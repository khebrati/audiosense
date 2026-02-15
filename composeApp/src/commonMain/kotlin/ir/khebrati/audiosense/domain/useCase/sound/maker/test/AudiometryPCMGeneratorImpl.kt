package ir.khebrati.audiosense.domain.useCase.sound.maker.test

import ir.khebrati.audiosense.domain.model.AcousticConstants.MAX_PCM_16BIT_VALUE
import ir.khebrati.audiosense.domain.model.AcousticConstants.MIN_PCM_16BIT_VALUE
import ir.khebrati.audiosense.domain.model.SoundPoint
import ir.khebrati.audiosense.domain.useCase.sound.maker.harmonic.HarmonicGenerator
import kotlin.time.Duration

class AudiometryPCMGeneratorImpl(private val harmonicGenerator: HarmonicGenerator) :
    AudiometryPCMGenerator {
    override fun generate(duration: Duration, soundPoint: SoundPoint) =
        harmonicGenerator
            .makeHarmonicWave(
                amplitude =
                    soundPoint.amplitude.coerceIn(
                        MIN_PCM_16BIT_VALUE.toFloat(),
                        MAX_PCM_16BIT_VALUE.toFloat(),
                    ),
                frequency = soundPoint.frequency,
                sampleRate = 44800,
                durationMillis = duration.inWholeMilliseconds.toFloat(),
                fadeRatio = 0.1f,
            )
            .map { it / (MAX_PCM_16BIT_VALUE + 1) }
            .toFloatArray()
}
