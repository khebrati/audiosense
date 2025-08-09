package ir.khebrati.audiosense.domain.useCase.sound.maker.test

interface TestSoundGenerator {
    fun makeTestSound(
        frequency: Int,
        amplitude: Float,
    ) : FloatArray
}
