package ir.khebrati.audiosense.domain.useCase.player

interface SoundPlayer {
    fun play(samples : FloatArray,sampleRate: Int,channel: AudioChannel)
}
