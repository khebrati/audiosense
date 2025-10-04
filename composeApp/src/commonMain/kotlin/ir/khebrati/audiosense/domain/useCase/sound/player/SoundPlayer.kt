package ir.khebrati.audiosense.domain.useCase.sound.player

interface SoundPlayer {
    suspend fun play(samples : FloatArray,sampleRate: Int = 44800,channel: AudioChannel)
}
