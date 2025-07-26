package ir.khebrati.audiosense.di

import androidx.room.RoomDatabase
import ir.khebrati.audiosense.data.source.AppDatabase
import ir.khebrati.audiosense.data.source.dao.HeadphoneDao
import ir.khebrati.audiosense.data.source.dao.TestDao
import ir.khebrati.audiosense.data.source.dao.TestHeadphoneDao
import ir.khebrati.audiosense.data.source.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun commonModule() : Module = module{
    //Database
    single<AppDatabase> {
        getRoomDatabase(
            get<RoomDatabase.Builder<AppDatabase>>()
        )
    }
    //Dao
    single<HeadphoneDao> { get<AppDatabase>().headphoneDao() }
    single<TestDao> { get<AppDatabase>().testDao() }
    single<TestHeadphoneDao> { get<AppDatabase>().testHeadphoneDao() }
}
expect fun platformModule() : Module