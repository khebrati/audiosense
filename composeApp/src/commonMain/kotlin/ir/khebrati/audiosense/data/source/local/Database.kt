@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package ir.khebrati.audiosense.data.source.local

import AudioSense.composeApp.BuildConfig
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
            Pair("1c7b54aa-61cd-487d-ac2f-7a41537a84e8", DefaultHeadphones.Uncalibrated.model),
        )
    headphones.forEach {
        connection.execSQL(
            "INSERT INTO \"main\".\"LocalHeadphone\" (\"id\", \"model\", \"calibrationCoefficients\") VALUES ('${it.first}', '${it.second}', '{\"125\":{\"first\":50,\"second\":50},\"250\":{\"first\":50,\"second\":50},\"500\":{\"first\":50,\"second\":50},\"1000\":{\"first\":50,\"second\":50},\"2000\":{\"first\":50,\"second\":50},\"4000\":{\"first\":50,\"second\":50},\"8000\":{\"first\":50,\"second\":50}}');"
        )
    }
    if(BuildConfig.IS_DEVELOPMENT){
        insertFakeTests(connection)
    }
}

private fun insertFakeTests(connection: SQLiteConnection) {
    val galaxyBudsId = "a15c6946-0f18-4ae0-82c1-16a7ef8dc4dc"
    val appleAirpodsId = "04cad680-777e-41a1-8770-f6bb5ed50ea8"
    val sonyHeadphonesId = "9165f20d-1ce6-4eb6-b2a8-0955dd8f6407"

    // Fake test 1: Normal hearing with Galaxy Buds FE
    connection.execSQL(
        """INSERT INTO "main"."LocalTest" ("id", "dateTime", "noiseDuringTest", "leftAC", "rightAC", "headphoneId", "personName", "personAge", "hasHearingAidExperience") 
           VALUES ('fake-test-001', '2025-12-20T10:30:00Z', 25, 
           '{"125":10,"250":10,"500":15,"1000":10,"2000":15,"4000":20,"8000":20}',
           '{"125":10,"250":15,"500":10,"1000":15,"2000":10,"4000":15,"8000":20}',
           '$galaxyBudsId', 'John Doe', 35, 0);"""
    )

    // Fake test 2: Mild hearing loss with Apple Airpods
    connection.execSQL(
        """INSERT INTO "main"."LocalTest" ("id", "dateTime", "noiseDuringTest", "leftAC", "rightAC", "headphoneId", "personName", "personAge", "hasHearingAidExperience") 
           VALUES ('fake-test-002', '2025-12-15T14:00:00Z', 30, 
           '{"125":25,"250":30,"500":35,"1000":30,"2000":35,"4000":40,"8000":45}',
           '{"125":20,"250":25,"500":30,"1000":35,"2000":30,"4000":35,"8000":40}',
           '$appleAirpodsId', 'Jane Smith', 45, 1);"""
    )

    // Fake test 3: Moderate hearing loss with Sony Headphones
    connection.execSQL(
        """INSERT INTO "main"."LocalTest" ("id", "dateTime", "noiseDuringTest", "leftAC", "rightAC", "headphoneId", "personName", "personAge", "hasHearingAidExperience") 
           VALUES ('fake-test-003', '2025-12-10T09:15:00Z', 20, 
           '{"125":40,"250":45,"500":50,"1000":55,"2000":50,"4000":55,"8000":60}',
           '{"125":45,"250":50,"500":55,"1000":50,"2000":55,"4000":60,"8000":65}',
           '$sonyHeadphonesId', 'Bob Wilson', 62, 1);"""
    )
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
