package ir.khebrati.audiosense.domain.useCase.sound.maker.test

import ir.khebrati.audiosense.domain.useCase.sound.maker.harmonic.HarmonicGenerator

class TestSoundGeneratorImpl(
    private val harmonicGenerator: HarmonicGenerator,
) : TestSoundGenerator {
    override fun makeTestSound(frequency: Int, amplitude: Float) : FloatArray{
        val maxAmplitude = 32768f
        return harmonicGenerator.makeHarmonicWave(
            amplitude = amplitude,
            frequency = frequency,
            sampleRate = 44800,
            durationSeconds = 1f,
            fadeRatio = 0.1f,
        ).map {
            it / maxAmplitude
        }.toFloatArray()
    }
}
