@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package ir.khebrati.audiosense.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import ir.khebrati.audiosense.data.source.local.dao.HeadphoneDao
import ir.khebrati.audiosense.data.source.local.dao.TestDao
import ir.khebrati.audiosense.data.source.local.dao.TestHeadphoneDao
import ir.khebrati.audiosense.data.source.local.entity.LocalHeadphone
import ir.khebrati.audiosense.data.source.local.entity.LocalTest
import ir.khebrati.audiosense.domain.model.DefaultHeadphones
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(connection: SQLiteConnection) {
                    super.onCreate(connection)
                    prepopulateDb(connection)
                }
            }
        )
        .build()
}

private fun prepopulateDb(connection: SQLiteConnection) {
    val headphones =
        listOf(
            Pair("a15c6946-0f18-4ae0-82c1-16a7ef8dc4dc", DefaultHeadphones.GalaxyBudsFE.model),
            Pair("04cad680-777e-41a1-8770-f6bb5ed50ea8", DefaultHeadphones.AppleAirpods.model),
            Pair("9165f20d-1ce6-4eb6-b2a8-0955dd8f6407", DefaultHeadphones.SonyHeadphones.model),
            Pair("1c7b54aa-61cd-487d-ac2f-7a41537a84e8", "Default"),
        )
    headphones.forEach {
        connection.execSQL(
            "INSERT INTO \"main\".\"LocalHeadphone\" (\"id\", \"model\", \"calibrationCoefficients\") VALUES ('${it.first}', '${it.second}', '{\"125\":{\"first\":50,\"second\":50},\"250\":{\"first\":50,\"second\":50},\"500\":{\"first\":50,\"second\":50},\"1000\":{\"first\":50,\"second\":50},\"2000\":{\"first\":50,\"second\":50},\"4000\":{\"first\":50,\"second\":50},\"8000\":{\"first\":50,\"second\":50}}');"
        )
    }
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
