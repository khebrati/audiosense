package ir.khebrati.audiosense.domain.useCase.sound.maker.harmonic

interface HarmonicGenerator {
    /**
     * Generates a harmonic wave with the formula:
     * `y(t) = A sin(2πf/fs t)` where :
     * - `A` is the amplitude,
     * - `f` is the frequency,
     * - `fs` is the sample rate,
     * - `t` is the time in seconds (it is multiplied by the sample rate to make it discrete).
     * This function also adds linear Fade In/ Fade out effect to a sound based on provided ratio.
     * @param fadeRatio A value between 0 and 0.5 that determines how many first and last samples are effected.
     * For example, if fadeRatio = 0.1, The first and last 10 percent of your sample array are decreased/increased
     * in volume.
     * @throws IllegalArgumentException if fadeRatio > 0.5 or < 0
     */
    fun makeHarmonicWave(
        amplitude: Float,
        frequency: Int,
        sampleRate: Int,
        durationSeconds: Float,
        fadeRatio: Float
    ): FloatArray


}