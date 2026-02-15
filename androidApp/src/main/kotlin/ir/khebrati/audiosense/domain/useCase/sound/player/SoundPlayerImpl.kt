package ir.khebrati.audiosense.domain.useCase.sound.player

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SoundPlayerImpl : SoundPlayer {
    override suspend fun play(duration: Duration, samples: FloatArray, sampleRate: Int, channel: AudioChannel) {
        val audioAttributes =
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        //TODO match given sample rate with device sample rate
        val audioFormat =
            AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build()
        val androidRecommendedBufferSize =
            AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_FLOAT,
            )
        // Make sure buffer size is more than input samples size
        val assuredBufferSize =
            max(samples.size * 4, androidRecommendedBufferSize) // 4 bytes per float
        val track =
            AudioTrack(audioAttributes, audioFormat, assuredBufferSize, AudioTrack.MODE_STATIC, 0)
        // Write the audio data to the AudioTrack
        track.write(samples, 0, samples.size, AudioTrack.WRITE_BLOCKING)
        when (channel) {
            AudioChannel.RIGHT -> track.setStereoVolume(0f, AudioTrack.getMaxVolume())
            AudioChannel.LEFT -> track.setStereoVolume(AudioTrack.getMaxVolume(), 0f)
            else -> {}
        }
        track.play()
        delay(duration)
    }
}