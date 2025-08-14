package ir.khebrati.audiosense.domain.useCase.sound.maker.test

import ir.khebrati.audiosense.domain.model.AcousticConstants.MAX_PCM_16BIT_VALUE
import ir.khebrati.audiosense.domain.model.AcousticConstants.MIN_PCM_16BIT_VALUE
import ir.khebrati.audiosense.domain.useCase.sound.maker.harmonic.HarmonicGenerator

class TestSoundGeneratorImpl(private val harmonicGenerator: HarmonicGenerator) :
    TestSoundGenerator {
    override fun makeTestSound(frequency: Int, amplitude: Float) =
        harmonicGenerator
            .makeHarmonicWave(
                amplitude =
                    amplitude.coerceIn(
                        MIN_PCM_16BIT_VALUE.toFloat(),
                        MAX_PCM_16BIT_VALUE.toFloat(),
                    ),
                frequency = frequency,
                sampleRate = 44800,
                durationSeconds = 5f,
                fadeRatio = 0.1f,
            )
            .map { it / (MAX_PCM_16BIT_VALUE + 1) }
            .toFloatArray()
}
