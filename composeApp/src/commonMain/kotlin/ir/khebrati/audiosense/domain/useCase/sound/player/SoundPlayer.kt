package ir.khebrati.audiosense.domain.useCase.sound.player

interface SoundPlayer {
    fun play(samples : FloatArray,sampleRate: Int,channel: AudioChannel)
}
