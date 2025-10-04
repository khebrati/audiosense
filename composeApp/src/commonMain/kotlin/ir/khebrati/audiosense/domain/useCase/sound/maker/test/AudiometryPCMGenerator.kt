package ir.khebrati.audiosense.domain.useCase.sound.maker.test

import ir.khebrati.audiosense.domain.model.SoundPoint

interface AudiometryPCMGenerator {
    fun generate(
        soundPoint: SoundPoint,
    ) : FloatArray
}
