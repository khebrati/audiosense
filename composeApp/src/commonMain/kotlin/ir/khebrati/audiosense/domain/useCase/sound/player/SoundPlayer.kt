package ir.khebrati.audiosense.domain.useCase.sound.player

import kotlin.time.Duration

interface SoundPlayer {
    suspend fun play(duration: Duration, samples : FloatArray, sampleRate: Int = 44800, channel: AudioChannel)
}
