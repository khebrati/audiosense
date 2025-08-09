package ir.khebrati.audiosense.domain.useCase.sound.maker.harmonic

import kotlin.math.PI
import kotlin.math.max
import kotlin.math.sin

class HarmonicGeneratorImpl : HarmonicGenerator {
    override fun makeHarmonicWave(
        amplitude: Float,
        frequency: Int,
        sampleRate: Int,
        durationSeconds: Float,
        fadeRatio: Float,
    ): FloatArray {
        val numOfSamples = (sampleRate * durationSeconds).toInt()
        val emptySoundSamples = FloatArray(numOfSamples)
        val angle = (2 * PI.toFloat() * frequency) / sampleRate
        val maxFadeInSampleIndex = max(1, (numOfSamples * fadeRatio).toInt())
        val generatedSoundSamples =
            emptySoundSamples.mapIndexed { index, value ->
                var value = amplitude * sin(angle * index)
                if (index < maxFadeInSampleIndex) {
                    value = value * index / maxFadeInSampleIndex
                }
                if (index > (numOfSamples - maxFadeInSampleIndex)) {
                    value = value * (numOfSamples - index) / maxFadeInSampleIndex
                }
                value
            }

        return generatedSoundSamples.toFloatArray()
    }
}