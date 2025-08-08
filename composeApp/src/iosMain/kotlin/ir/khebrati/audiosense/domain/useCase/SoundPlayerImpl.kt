package ir.khebrati.audiosense.domain.useCase

import ir.khebrati.audiosense.domain.useCase.player.AudioChannel
import ir.khebrati.audiosense.domain.useCase.player.SoundPlayer

class SoundPlayerImpl : SoundPlayer {
    override fun play(samples: FloatArray,sampleRate:Int, channel: AudioChannel) {
        TODO("Not yet implemented")
    }
}
