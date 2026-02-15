package ir.khebrati.audiosense.di

import ir.khebrati.audiosense.domain.useCase.share.ShareService
import ir.khebrati.audiosense.domain.useCase.share.ShareServiceImpl
import ir.khebrati.audiosense.domain.useCase.sound.player.SoundPlayerImpl
import ir.khebrati.audiosense.domain.useCase.sound.player.SoundPlayer
import org.koin.dsl.module

fun androidPlatformModule() = module {
    factory<SoundPlayer>{ SoundPlayerImpl() }
    factory<ShareService>{ ShareServiceImpl(inject()) }
}