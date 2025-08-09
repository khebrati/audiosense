package ir.khebrati.audiosense.integration

import ir.khebrati.audiosense.domain.useCase.sound.maker.harmonic.HarmonicGeneratorImpl
import ir.khebrati.audiosense.domain.useCase.sound.player.AudioChannel
import ir.khebrati.audiosense.domain.useCase.sound.player.SoundPlayerImpl
import kotlin.test.Test

class PlayHarmonicTest {
    @Test
    fun harmonicGeneratorSoundPlayer_canPlayHarmonicSounds_validatedByHearing(){
        val soundMaker = createHarmonicGenerator()
        val soundPlayer = createSoundPlayer()

        //Params
        val frequency = 4000
        val amplitude = 32768f
        val sampleRate = 48000
        val duration = 5f
        val fadeInRatio = 0.1f

        //GenerateSound
        val soundSamples = soundMaker.makeHarmonicWave(
            amplitude = amplitude,
            frequency = frequency,
            sampleRate = sampleRate,
            durationSeconds = duration,
            fadeRatio =fadeInRatio
        )
        //Play
        soundPlayer.play(
            samples = soundSamples.map {
                it / amplitude
            }.toFloatArray(),
            sampleRate = sampleRate,
            channel = AudioChannel.RIGHT
        )
    }

    private fun createHarmonicGenerator() = HarmonicGeneratorImpl()
    private fun createSoundPlayer() = SoundPlayerImpl()
}