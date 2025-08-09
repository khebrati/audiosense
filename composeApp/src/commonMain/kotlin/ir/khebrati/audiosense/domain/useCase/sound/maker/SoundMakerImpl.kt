package ir.khebrati.audiosense.domain.useCase.sound.maker

import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class SoundMakerImpl : SoundMaker {
    override fun makeHarmonicWave(
        amplitude: Double,
        frequency: Int,
        sampleRate: Int,
        durationSeconds: Float,
        fadeRatio: Float,
    ): DoubleArray {
        val numOfSamples = (sampleRate * durationSeconds).toInt()
        val emptySoundSamples = FloatArray(numOfSamples)
        val angle = (2 * PI * frequency) / sampleRate
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

        return generatedSoundSamples.toDoubleArray()
    }
}
