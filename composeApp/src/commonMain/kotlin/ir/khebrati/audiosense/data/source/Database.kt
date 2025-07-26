@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package ir.khebrati.audiosense.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import ir.khebrati.audiosense.data.source.dao.HeadphoneDao
import ir.khebrati.audiosense.data.source.dao.TestDao
import ir.khebrati.audiosense.data.source.dao.TestHeadphoneDao
import ir.khebrati.audiosense.data.source.entity.LocalHeadphone
import ir.khebrati.audiosense.data.source.entity.LocalTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO


fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder.setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
}

@TypeConverters(Convertors::class)
@Database(entities = [LocalHeadphone::class, LocalTest::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun headphoneDao(): HeadphoneDao
    abstract fun testDao(): TestDao
    abstract fun testHeadphoneDao(): TestHeadphoneDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}