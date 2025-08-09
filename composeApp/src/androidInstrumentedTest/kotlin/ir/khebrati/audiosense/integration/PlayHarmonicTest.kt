package ir.khebrati.audiosense.integration

import ir.khebrati.audiosense.domain.useCase.sound.maker.SoundMakerImpl
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
        val amplitude = 32768f.toDouble()
        val sampleRate = 44100
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
                val value = it.toFloat() / amplitude.toFloat()
                if(value > 2){
                    value
                }else value
            }.toFloatArray(),
            sampleRate = sampleRate,
            channel = AudioChannel.RIGHT
        )
    }

    private fun createHarmonicGenerator() = SoundMakerImpl()
    private fun createSoundPlayer() = SoundPlayerImpl()
}