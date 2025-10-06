package ir.khebrati.audiosense.domain.useCase.sound.maker.test

import ir.khebrati.audiosense.domain.model.SoundPoint
import kotlin.time.Duration

interface AudiometryPCMGenerator {
    fun generate(
        duration: Duration,
        soundPoint: SoundPoint,
    ) : FloatArray
}
