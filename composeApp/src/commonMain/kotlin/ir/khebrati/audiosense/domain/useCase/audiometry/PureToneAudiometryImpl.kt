package ir.khebrati.audiosense.domain.useCase.audiometry

import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.domain.model.Side
import ir.khebrati.audiosense.domain.model.SoundPoint
import ir.khebrati.audiosense.domain.useCase.spl.dbSpl
import ir.khebrati.audiosense.domain.useCase.spl.calculateLossBasedOnDbHl
import ir.khebrati.audiosense.domain.useCase.spl.dbHl
import kotlin.collections.hashMapOf
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

internal typealias AC = Map<Int, Int>

class PureToneAudiometryImpl(
    val minDbHl: Int = -10,
    val maxDbHl: Int = 90,
    startingDbSpl: Int = 30,
    val adjustmentsOrder: List<AcousticAdjustment> =
        listOf(
            AcousticAdjustment(20, Direction.UP),
            AcousticAdjustment(10, Direction.DOWN),
            AcousticAdjustment(5, Direction.UP),
        ),
    val minWaitTimeSeconds: Int = 2,
    val maxWaitTimeSeconds: Int = 5,
    val frequencies: List<Int> = AcousticConstants.allFrequencyOctaves,
    val logger: Logger,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) : PureToneAudiometry {
    init {
        if (startingDbSpl < minDbHl || startingDbSpl > maxDbHl) {
            throw IllegalArgumentException("startingDbHl must be between min/max dbHL values")
        }
    }

    private val _currentSide = MutableStateFlow(Side.LEFT)
    override val currentSide: StateFlow<Side> = _currentSide.asStateFlow()

    private val _soundToPlay = MutableSharedFlow<SoundPoint>(replay = 1)
    override val soundToPlay: SharedFlow<SoundPoint> = _soundToPlay.asSharedFlow()

    private var callbackWhenDone: ((Map<Int, Int>, Map<Int, Int>) -> Unit)? = null
    private var lastPlayedSoundHeard: Boolean = false

    private val acResults: Pair<HashMap<Int, Int>, HashMap<Int, Int>> =
        Pair(hashMapOf(), hashMapOf())
    private var currentAdjustmentIndex = 0
    private val currentAdjustment
        get() = adjustmentsOrder[currentAdjustmentIndex]

    private val initialFreqIndex = 0
    private val _currentFrequencyIndex = MutableStateFlow(initialFreqIndex)
    private val currentFrequency
        get() = frequencies[_currentFrequencyIndex.value]

    private val startingDbHl = startingDbSpl.dbHl(currentFrequency)
    private var currentDb: Int = startingDbHl
    override val progress: StateFlow<Float> =
        _currentFrequencyIndex
            .map {
                val partial = (it.toFloat() / frequencies.size) / 2
                if (_currentSide.value == Side.LEFT) {
                    partial
                } else {
                    0.5f + partial
                }
            }
            .onEach { logger.v { "Progress: $it" } }
            .stateIn(scope = scope, started = SharingStarted.WhileSubscribed(), initialValue = 0f)
    private var done: Boolean = false

    override suspend fun start(calibrationCoefficients: Map<Int, Int>) {
        while (!done) {
            setCurrentDbForPlay()
            logger.v { "Waiting for heard response" }
            delay(generateRandomDuration())
            changeAdjustmentIfNeeded()
            if (!done) {
                incOrDecSoundBasedOnCurrentAdjustment()
                checkMinMaxLimits()
            }
        }
    }

    private fun checkMinMaxLimits() {
        if (currentDb >= maxDbHl || currentDb <= minDbHl) {
            logger.v { "Current db $currentDb is out of range $maxDbHl $minDbHl" }
            setCurrentDbAsThresh()
            goNextFrequency()
        }
    }

    private fun changeAdjustmentIfNeeded() {
        val direction = currentAdjustment.direction
        if (
            direction == Direction.UP && lastPlayedSoundHeard ||
                direction == Direction.DOWN && !lastPlayedSoundHeard
        ) {
            logger.v { "Changing direction" }
            currentAdjustmentIndex++
            if (currentAdjustmentIndex >= adjustmentsOrder.size) {
                logger.v { "No more adjustments in the list" }
                setCurrentDbAsThresh()
                goNextFrequency()
            } else {
                logger.v { "Current adjustment is $currentAdjustment" }
            }
        }
    }

    private fun setCurrentDbAsThresh() {
        val side = _currentSide.value
        val map = if (side == Side.LEFT) acResults.first else acResults.second
        map[currentFrequency] = currentDb
        logger.v {
            "Set $currentDb as thresh for freq $currentFrequency on side ${_currentSide.value}"
        }
    }

    private fun goNextFrequency() {
        val newFreqIndex = _currentFrequencyIndex.value + 1
        if (newFreqIndex >= frequencies.size) {
            logger.v { "No more frequencies for side ${_currentSide.value}" }
            if (_currentSide.value == Side.RIGHT) {
                val results = getDbHlResults()
                callbackWhenDone?.let { it(results.first,results.second) }
                done = true
                logger.v {
                    "Audiometry operation completed. Results: ${acResults.first} ${acResults.second}"
                }
                _currentFrequencyIndex.update { newFreqIndex }
                return
            }
            _currentSide.update { Side.RIGHT }
            logger.v { "Changed side to ${Side.RIGHT}" }
            _currentFrequencyIndex.update { initialFreqIndex }
            logger.v { "Reset freq to ${currentFrequency}" }
        } else {
            _currentFrequencyIndex.update { newFreqIndex }
        }
        currentAdjustmentIndex = 0
        currentDb = startingDbHl
        logger.v { "Reset adjustment to $currentAdjustment and current db to $currentDb" }
    }

    private fun getDbHlResults() = Pair(calculateLossBasedOnDbHl(acResults.first), calculateLossBasedOnDbHl(acResults.second))

    private suspend fun setCurrentDbForPlay() {
        val soundPoint = SoundPoint(frequency = currentFrequency, amplitude = currentDb.dbSpl)
        lastPlayedSoundHeard = false
        logger.v { "Sending $soundPoint for play" }
        _soundToPlay.emit(soundPoint)
        logger.v { "Sent $soundPoint" }
    }

    private fun incOrDecSoundBasedOnCurrentAdjustment() {
        val step = currentAdjustment.amplitudeStepDbHl
        val newDb =
            if (currentAdjustment.direction == Direction.UP) {
                currentDb + step
            } else {
                currentDb - step
            }
        logger.v { "Increased ${currentAdjustment.direction.name} with step: $step" }
        currentDb = newDb
        logger.v { "Current db: $currentDb" }
    }

    private fun generateRandomDuration() =
        Random(43).nextLong(minWaitTimeSeconds.toLong(), maxWaitTimeSeconds.toLong()).seconds

    override fun onHeard() {
        lastPlayedSoundHeard = true
    }

    override fun performActionWhenFinished(action: (Map<Int, Int>, Map<Int, Int>) -> Unit) {
        callbackWhenDone = action
    }
}

data class AcousticAdjustment(val amplitudeStepDbHl: Int, val direction: Direction)

enum class Direction {
    UP,
    DOWN,
}
