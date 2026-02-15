package ir.khebrati.audiosense.di

import androidx.room.RoomDatabase
import ir.khebrati.audiosense.data.source.local.AppDatabase
import ir.khebrati.audiosense.data.source.getDatabaseBuilder
import ir.khebrati.audiosense.domain.useCase.share.ShareService
import ir.khebrati.audiosense.domain.useCase.share.ShareServiceImpl
import ir.khebrati.audiosense.domain.useCase.sound.player.SoundPlayerImpl
import ir.khebrati.audiosense.domain.useCase.sound.player.SoundPlayer
import org.koin.dsl.module

actual fun platformModule() = module {
    factory<RoomDatabase.Builder<AppDatabase>>{ getDatabaseBuilder(inject()) }
    factory<SoundPlayer>{ SoundPlayerImpl() }
    factory<ShareService>{ ShareServiceImpl(inject()) }
}