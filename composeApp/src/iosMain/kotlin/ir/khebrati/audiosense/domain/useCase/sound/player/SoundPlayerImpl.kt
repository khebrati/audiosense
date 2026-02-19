package ir.khebrati.audiosense.domain.useCase.sound.player
import kotlinx.cinterop.*
import kotlinx.coroutines.delay
import platform.AVFAudio.*
import platform.CoreAudioTypes.*
import kotlin.time.Duration

class SoundPlayerImpl : SoundPlayer {
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun play(
        duration: Duration,
        samples: FloatArray,
        sampleRate: Int,
        channel: AudioChannel
    ) {
        memScoped {
            // Set up the audio session
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryPlayback, error = null)
            audioSession.setActive(true, error = null)

            // Set up the audio format description
            val streamDescription = alloc<AudioStreamBasicDescription>()
            streamDescription.mSampleRate = sampleRate.toDouble()
            streamDescription.mFormatID = kAudioFormatLinearPCM
            streamDescription.mFormatFlags =
                kAudioFormatFlagIsFloat or kAudioFormatFlagIsPacked
            streamDescription.mBitsPerChannel = 32u
            streamDescription.mChannelsPerFrame = 1u  // Mono
            streamDescription.mFramesPerPacket = 1u
            streamDescription.mBytesPerFrame = 4u     // 4 bytes per float
            streamDescription.mBytesPerPacket = 4u

            // Create AVAudioFormat from the stream description
            val audioFormat = AVAudioFormat(streamDescription.ptr)

            // Create the AVAudioEngine and player node
            val engine = AVAudioEngine()
            val playerNode = AVAudioPlayerNode()
            engine.attachNode(playerNode)

            // Get the output node for channel mapping
            val outputNode = engine.outputNode
            val outputFormat = outputNode.inputFormatForBus(0u)

            // Connect player to output
            engine.connect(playerNode, to = outputNode, format = audioFormat)

            // Create the PCM buffer
            val frameCount = samples.size.toUInt()
            val buffer = AVAudioPCMBuffer(audioFormat, frameCount)
                ?: error("Failed to create AVAudioPCMBuffer")
            buffer.frameLength = frameCount

            // Copy samples into the buffer
            val channelData = buffer.floatChannelData
                ?: error("Failed to get float channel data")
            val channelPtr = channelData[0]
                ?: error("Failed to get channel pointer")

            samples.forEachIndexed { index, sample ->
                channelPtr[index] = sample
            }

            // Apply channel volume (LEFT / RIGHT / BOTH)
            when (channel) {
                AudioChannel.LEFT -> {
                    playerNode.pan = -1.0f  // Full left
                }
                AudioChannel.RIGHT -> {
                    playerNode.pan = 1.0f   // Full right
                }
                else -> {
                    playerNode.pan = 0.0f   // Center (both channels)
                }
            }

            // Start the engine and play
            engine.startAndReturnError(null)
            playerNode.scheduleBuffer(buffer, completionHandler = null)
            playerNode.play()

            // Wait for the duration
            delay(duration)

            // Cleanup
            playerNode.stop()
            engine.stop()
            audioSession.setActive(false, error = null)
        }
    }
}