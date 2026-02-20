@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package ir.khebrati.audiosense.data.source.local

import AudioSense.composeApp.BuildConfig
import androidx.room.ConstructedBy
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
import ir.khebrati.audiosense.domain.model.DefaultHeadphonesName
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
                    if (BuildConfig.IS_DEVELOPMENT) prepopulateDb(connection)
                }
            }
        )
        .build()
}

private fun prepopulateDb(connection: SQLiteConnection) {
    insertDefaultHeadphones(connection)
    insertFakeTests(connection)
}

private fun insertDefaultHeadphones(connection: SQLiteConnection) {
    data class DefaultHeadphone(
        val id: String,
        val name: DefaultHeadphonesName,
        val calibration: Map<Int, Int>,
    )

    val defaultHeadphones =
        listOf(
            DefaultHeadphone(
                id = "a15c6946-0f18-4ae0-82c1-16a7ef8dc4dc",
                name = DefaultHeadphonesName.GalaxyBudsFE,
                calibration = mapOf(
                    250 to -15,
                    500 to -10,
                    1000 to 5,
                    2000 to 0,
                    4000 to 0,
                    8000 to 15,
                ),
            ),
//            DefaultHeadphone(
//                id = "04cad680-777e-41a1-8770-f6bb5ed50ea8",
//                name = DefaultHeadphonesName.AppleAirpods,
//                calibration = mapOf(),
//            ),
//            DefaultHeadphone(
//                id = "9165f20d-1ce6-4eb6-b2a8-0955dd8f6407",
//                name = DefaultHeadphonesName.SonyHeadphones,
//                calibration = mapOf(),
//            ),
            DefaultHeadphone(
                id = "1c7b54aa-61cd-487d-ac2f-7a41537a84e8",
                name = DefaultHeadphonesName.Uncalibrated,
                calibration = mapOf(
                    250 to 0,
                    1000 to 0,
                    500 to 0,
                    8000 to 0,
                    4000 to 0,
                    2000 to 0
                ),
            ),
        )
    defaultHeadphones.forEach {
        val calibration = it.calibration
        connection.execSQL(
            "INSERT INTO \"main\".\"LocalHeadphone\" (\"id\", \"model\", \"calibrationCoefficients\") VALUES ('${it.id}', '${it.name.value}', '{\"250\":${calibration[250]},\"500\":${calibration[500]},\"1000\":${calibration[1000]},\"2000\":${calibration[2000]},\"4000\":${calibration[4000]},\"8000\":${calibration[8000]}}');"
        )
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
           '{"250":10,"500":15,"1000":10,"2000":15,"4000":20,"8000":20}',
           '{"250":15,"500":10,"1000":15,"2000":10,"4000":15,"8000":20}',
           '$galaxyBudsId', 'John Doe', 35, 0);"""
    )

    // Fake test 2: Mild hearing loss with Apple Airpods
    connection.execSQL(
        """INSERT INTO "main"."LocalTest" ("id", "dateTime", "noiseDuringTest", "leftAC", "rightAC", "headphoneId", "personName", "personAge", "hasHearingAidExperience") 
           VALUES ('fake-test-002', '2025-12-15T14:00:00Z', 30, 
           '{"250":30,"500":35,"1000":30,"2000":35,"4000":40,"8000":45}',
           '{"250":25,"500":30,"1000":35,"2000":30,"4000":35,"8000":40}',
           '$appleAirpodsId', 'Jane Smith', 45, 1);"""
    )

    // Fake test 3: Moderate hearing loss with Sony Headphones
    connection.execSQL(
        """INSERT INTO "main"."LocalTest" ("id", "dateTime", "noiseDuringTest", "leftAC", "rightAC", "headphoneId", "personName", "personAge", "hasHearingAidExperience") 
           VALUES ('fake-test-003', '2025-12-10T09:15:00Z', 20, 
           '{"250":45,"500":50,"1000":55,"2000":50,"4000":55,"8000":60}',
           '{"250":50,"500":55,"1000":50,"2000":55,"4000":60,"8000":65}',
           '$sonyHeadphonesId', 'Bob Wilson', 62, 1);"""
    )
}

@TypeConverters(Convertors::class)
@Database(entities = [LocalHeadphone::class, LocalTest::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun headphoneDao(): HeadphoneDao

    abstract fun testDao(): TestDao

    abstract fun testHeadphoneDao(): TestHeadphoneDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
