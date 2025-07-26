package ir.khebrati.audiosense.di

import androidx.room.RoomDatabase
import ir.khebrati.audiosense.data.source.AppDatabase
import ir.khebrati.audiosense.data.source.dao.HeadphoneDao
import org.koin.core.module.Module
import org.koin.dsl.module

internal val commonModule : Module = module{
    //Database
    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>().build()
    }
    //Dao
    single<HeadphoneDao> { get<AppDatabase>().headphoneDao() }
}
expect fun platformModule() : Module