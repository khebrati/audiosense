package ir.khebrati.audiosense.domain.useCase.audiometry

import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.domain.model.Side
import ir.khebrati.audiosense.domain.model.SoundPoint
import ir.khebrati.audiosense.domain.useCase.spl.dbSpl
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PureToneAudiometryImpl(
    val minDbHl: Int = -10,
    val maxDbHl: Int = 90,
    val startingDbHl: Int = 50,
    val adjustmentsOrder: List<AcousticAdjustment> =
        listOf(
            AcousticAdjustment(20, Direction.UP),
            AcousticAdjustment(10, Direction.DOWN),
            AcousticAdjustment(5, Direction.UP),
        ),
    val frequencies: List<Int> = AcousticConstants.allFrequencyOctaves,
    val logger: Logger,
) : PureToneAudiometry {
    init {
        if (startingDbHl < minDbHl || startingDbHl > maxDbHl) {
            throw IllegalArgumentException("startingDbHl must be between min/max dbHL values")
        }
    }

    private val _currentSide = MutableStateFlow(Side.LEFT)
    override val currentSide: StateFlow<Side> = _currentSide.asStateFlow()

    private val _soundToPlay = MutableSharedFlow<SoundPoint>(replay = 1)
    override val soundToPlay: SharedFlow<SoundPoint> = _soundToPlay.asSharedFlow()

    private var callbackWhenDone: ((Map<Int, Int>, Map<Int, Int>) -> Unit)? = null
    private var lastPlayedSoundHeard: Boolean = false
    private var currentDb: Int = startingDbHl

    private val acResults: Pair<HashMap<Int, Int>, HashMap<Int, Int>> =
        Pair(hashMapOf(), hashMapOf())
    private var currentAdjustmentIndex = 0
    private val currentAdjustment
        get() = adjustmentsOrder[currentAdjustmentIndex]

    private var currentFrequencyIndex = frequencies.first()
    private val currentFrequency
        get() = frequencies[currentFrequencyIndex]

    override val progress: StateFlow<Float> = TODO("Based on freq/all Frequencies")
    private var done : Boolean = false


    override suspend fun start(calibrationCoefficients: Map<Int, Int>) {
        while(!done){
            delay(2.seconds)
            changeAdjustmentIfNeeded()

        }
    }

    private fun changeAdjustmentIfNeeded() {
        val direction = currentAdjustment.direction
        if (
            direction == Direction.UP && lastPlayedSoundHeard ||
                direction == Direction.DOWN && !lastPlayedSoundHeard
        ) {
            currentAdjustmentIndex++
            markAsThreshIfNeeded()
        }
        incOrDecSoundBasedOnCurrentAdjustment()
        TODO("Check min/max limits")
    }
    private fun markAsThreshIfNeeded(){
        if(currentAdjustmentIndex >= adjustmentsOrder.size){
            setCurrentDbAsThresh()
            goNextFrequency()
        }
    }
    private fun setCurrentDbAsThresh(){
        val side=  _currentSide.value
        val map = if(side == Side.LEFT) acResults.first else acResults.second
        map[currentFrequency] = currentDb
    }
    private fun goNextFrequency(){
        currentFrequencyIndex++
        if(currentFrequencyIndex >= frequencies.size){
            if(_currentSide.value == Side.RIGHT){
                callbackWhenDone?.let { it(acResults.first,acResults.second) }
                done = true
                return
            }
            _currentSide.update { Side.RIGHT }
        }
        currentAdjustmentIndex = 0
        currentDb = startingDbHl
    }

    private suspend fun setCurrentDbForPlay() {
        val soundPoint = SoundPoint(frequency = currentFrequency, amplitude = currentDb.dbSpl)
        lastPlayedSoundHeard = false
        _soundToPlay.emit(soundPoint)
    }

    private fun incOrDecSoundBasedOnCurrentAdjustment() {
        val step = currentAdjustment.amplitudeStepDbHl
        val newDb =
            if (currentAdjustment.direction == Direction.UP) {
                currentDb + step
            } else {
                currentDb - step
            }
        currentDb = newDb
    }


    private fun generateRandomDuration() = Random(43).nextLong(1, 2).seconds

    override fun onHeard() {
        lastPlayedSoundHeard = true
    }

    override fun setCallbackWhenDone(action: (Map<Int, Int>, Map<Int, Int>) -> Unit) {
        callbackWhenDone = action
    }
}

data class AcousticAdjustment(val amplitudeStepDbHl: Int, val direction: Direction)

enum class Direction {
    UP,
    DOWN,
}
