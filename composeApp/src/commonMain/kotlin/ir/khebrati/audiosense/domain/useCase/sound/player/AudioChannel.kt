package ir.khebrati.audiosense.domain.useCase.sound.player

import ir.khebrati.audiosense.domain.model.Side

enum class AudioChannel {
    LEFT,
    RIGHT,
    STEREO;
}

fun Side.toAudioChannel(): AudioChannel {
    return when (this) {
        Side.LEFT -> AudioChannel.LEFT
        Side.RIGHT -> AudioChannel.RIGHT
    }
}