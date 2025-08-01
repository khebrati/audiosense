package ir.khebrati.audiosense.di

import androidx.room.RoomDatabase
import ir.khebrati.audiosense.data.repository.HeadphoneRepositoryImpl
import ir.khebrati.audiosense.data.repository.TestRepositoryImpl
import ir.khebrati.audiosense.data.source.local.AppDatabase
import ir.khebrati.audiosense.data.source.local.dao.HeadphoneDao
import ir.khebrati.audiosense.data.source.local.dao.TestDao
import ir.khebrati.audiosense.data.source.local.dao.TestHeadphoneDao
import ir.khebrati.audiosense.data.source.local.getRoomDatabase
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import ir.khebrati.audiosense.domain.repository.TestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
    //Repository
    single<HeadphoneRepository>{ HeadphoneRepositoryImpl(headphoneDao = get(), dispatcher = Dispatchers.IO) }
    single<TestRepository>{ TestRepositoryImpl(testDao = get(), testHeadphoneDao = get(), dispatcher = Dispatchers.IO) }
}
expect fun platformModule() : Module