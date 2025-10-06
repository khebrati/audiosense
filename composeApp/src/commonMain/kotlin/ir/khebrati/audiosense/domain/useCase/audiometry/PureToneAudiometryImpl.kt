package ir.khebrati.audiosense.domain.useCase.audiometry

import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.domain.model.Side
import ir.khebrati.audiosense.domain.model.SoundPoint
import ir.khebrati.audiosense.domain.useCase.spl.dbSpl
import ir.khebrati.audiosense.domain.useCase.spl.fromDbSpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

class PureToneAudiometryImpl(
    val logger: Logger
) : PureToneAudiometry {
    private var _callbackWhenDone : ((Map<Int, Int>, Map<Int, Int>) -> Unit)? = null
    private val _progress = MutableStateFlow(0f)
    override val progress: StateFlow<Float> = _progress.asStateFlow()
    private val _currentSide = MutableStateFlow(Side.LEFT)
    override val currentSide: StateFlow<Side> = _currentSide.asStateFlow()

    private val _sounds = MutableSharedFlow<SoundPoint>(replay = 1)
    override val sounds: SharedFlow<SoundPoint> = _sounds.asSharedFlow()

    override suspend fun start(calibrationCoefficients: Map<Int, Int>) {
        val size = AcousticConstants.allFrequencyOctaves.size
        AcousticConstants.allFrequencyOctaves.forEach { freq ->
            delay(5.seconds)
            _sounds.emit(SoundPoint(freq, 60.dbSpl))
            logger.i { "Played $freq 70f" }
            _progress.update { it + 1/size }
        }
    }

    override fun onHeard() {
        if(_sounds.replayCache.isEmpty()) return
        logger.i { "Heard ${_sounds.replayCache.first()}" }
    }

    override fun callbackWhenDone(action: (Map<Int, Int>, Map<Int, Int>) -> Unit) {
        _callbackWhenDone = action
    }
}