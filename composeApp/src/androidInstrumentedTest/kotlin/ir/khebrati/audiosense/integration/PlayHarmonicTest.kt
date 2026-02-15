package ir.khebrati.audiosense.integration

import ir.khebrati.audiosense.domain.model.Side
import ir.khebrati.audiosense.domain.model.SoundPoint
import ir.khebrati.audiosense.domain.useCase.sound.maker.harmonic.HarmonicGeneratorImpl
import ir.khebrati.audiosense.domain.useCase.sound.maker.test.AudiometryPCMGeneratorImpl
import ir.khebrati.audiosense.domain.useCase.sound.player.AudioChannel
import ir.khebrati.audiosense.domain.useCase.sound.player.SoundPlayerImpl
import ir.khebrati.audiosense.domain.useCase.sound.player.toAudioChannel
import ir.khebrati.audiosense.domain.useCase.spl.dbSpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class PlayHarmonicTest {
    @Test
    fun harmonicGeneratorSoundPlayer_canPlayHarmonicSounds_validatedByHearing() = runBlocking{
        val soundMaker = createHarmonicGenerator()
        val soundPlayer = createSoundPlayer()

        //Params
        val frequency = 4000
        val amplitude = 32768f
        val sampleRate = 48000
        val duration = 5000f
        val fadeInRatio = 0.1f

        //GenerateSound
        val soundSamples = soundMaker.makeHarmonicWave(
            amplitude = amplitude,
            frequency = frequency,
            sampleRate = sampleRate,
            durationMillis = duration,
            fadeRatio =fadeInRatio
        )
        //Play
        soundPlayer.play(
            samples = soundSamples.map {
                it / amplitude
            }.toFloatArray(),
            sampleRate = sampleRate,
            duration = 2.seconds,
            channel = AudioChannel.RIGHT
        )
    }

    private fun createHarmonicGenerator() = HarmonicGeneratorImpl()
    private fun createSoundPlayer() = SoundPlayerImpl()

    @Test
    fun playRepeat_1000k_30() = runBlocking{
        playRepeatedlyToCompareWithHearingTestApp(
            point = SoundPoint(
                frequency = 1000,
                amplitude = 40.dbSpl,
            ),
            side = Side.RIGHT,
        )
    }
    suspend fun playRepeatedlyToCompareWithHearingTestApp(point: SoundPoint,side: Side) = runBlocking{
            val pcmGenerator = AudiometryPCMGeneratorImpl(HarmonicGeneratorImpl())
            val soundPlayer = SoundPlayerImpl()
            val duration = 0.3.seconds
            val pcm = pcmGenerator.generate(duration, point)
            soundPlayer.play(
                samples = pcm,
                duration = duration,
                channel = side.toAudioChannel(),
            )
            delay(0.3.seconds)
    }
}