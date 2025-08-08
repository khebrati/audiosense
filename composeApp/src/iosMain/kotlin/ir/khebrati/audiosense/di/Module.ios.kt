package ir.khebrati.audiosense.di

import androidx.room.RoomDatabase
import ir.khebrati.audiosense.data.source.local.AppDatabase
import ir.khebrati.audiosense.data.source.getDatabaseBuilder
import ir.khebrati.audiosense.domain.useCase.SoundPlayerImpl
import ir.khebrati.audiosense.domain.useCase.player.SoundPlayer
import org.koin.dsl.module

actual fun platformModule() = module {
    factory<RoomDatabase.Builder<AppDatabase>> { getDatabaseBuilder() }
    factory<SoundPlayer>{ SoundPlayerImpl() }
}