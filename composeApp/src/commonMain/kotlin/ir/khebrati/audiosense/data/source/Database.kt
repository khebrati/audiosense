package ir.khebrati.audiosense.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import ir.khebrati.audiosense.data.source.dao.HeadphoneDao
import ir.khebrati.audiosense.data.source.entity.LocalHeadphone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO


fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder.setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
}

@Database(entities = [LocalHeadphone::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun headphoneDao(): HeadphoneDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}