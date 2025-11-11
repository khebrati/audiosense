package ir.khebrati.audiosense.domain.useCase.sound.player

import kotlin.time.Duration

class SoundPlayerImpl : SoundPlayer {
    override suspend fun play(
        duration: Duration,
        samples: FloatArray,
        sampleRate: Int,
        channel: AudioChannel
    ) {
        TODO("Not yet implemented")
    }
}