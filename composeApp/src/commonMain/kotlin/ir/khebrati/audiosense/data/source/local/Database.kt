package ir.khebrati.audiosense.data.source.local

import AudioSense.composeApp.BuildConfig
import app.cash.sqldelight.db.SqlDriver
import ir.khebrati.audiosense.db.AudiosenseDb
import ir.khebrati.audiosense.db.LocalHeadphone
import ir.khebrati.audiosense.db.LocalTest
import ir.khebrati.audiosense.domain.model.DefaultHeadphonesName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun createDatabase(driver: SqlDriver): AudiosenseDb {
    val database = AudiosenseDb(
        driver = driver,
        LocalHeadphoneAdapter = LocalHeadphone.Adapter(
            calibrationCoefficientsAdapter = MapIntIntAdapter
        ),
        LocalTestAdapter = LocalTest.Adapter(
            dateTimeAdapter = InstantAdapter,
            leftACAdapter = MapIntIntAdapter,
            rightACAdapter = MapIntIntAdapter,
        )
    )

    if (BuildConfig.IS_DEVELOPMENT) {
        CoroutineScope(Dispatchers.Default).launch {
            prepopulateDb(database)
        }
    }

    return database
}

private suspend fun prepopulateDb(database: AudiosenseDb) {
    insertDefaultHeadphones(database)
    insertFakeTests(database)
}

private suspend fun insertDefaultHeadphones(database: AudiosenseDb) {
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
                    250 to 10,
                    1000 to 10,
                    500 to 5,
                    8000 to 10,
                    4000 to 10,
                    2000 to 10
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
                    1000 to 10,
                    500 to 0,
                    8000 to 5,
                    4000 to 5,
                    2000 to 10
                ),
            ),
        )

    defaultHeadphones.forEach {
        database.localHeadphoneQueries.addHeadphoneOrIgnore(
            id = it.id,
            model = it.name.value,
            calibrationCoefficients = it.calibration
        )
    }
}

private suspend fun insertFakeTests(database: AudiosenseDb) {
    val galaxyBudsId = "a15c6946-0f18-4ae0-82c1-16a7ef8dc4dc"
    val appleAirpodsId = "04cad680-777e-41a1-8770-f6bb5ed50ea8"
    val sonyHeadphonesId = "9165f20d-1ce6-4eb6-b2a8-0955dd8f6407"

    // Fake test 1: Normal hearing with Galaxy Buds FE
    database.localTestQueries.addTestOrIgnore(
        id = "fake-test-001",
        dateTime = kotlin.time.Instant.parse("2025-12-20T10:30:00Z"),
        noiseDuringTest = 25,
        leftAC = mapOf(250 to 10, 500 to 15, 1000 to 10, 2000 to 15, 4000 to 20, 8000 to 20),
        rightAC = mapOf(250 to 15, 500 to 10, 1000 to 15, 2000 to 10, 4000 to 15, 8000 to 20),
        headphoneId = galaxyBudsId,
        personName = "John Doe",
        personAge = 35,
        hasHearingAidExperience = false
    )

    // Fake test 2: Mild hearing loss with Apple Airpods
    database.localTestQueries.addTestOrIgnore(
        id = "fake-test-002",
        dateTime = kotlin.time.Instant.parse("2025-12-15T14:00:00Z"),
        noiseDuringTest = 30,
        leftAC = mapOf(250 to 30, 500 to 35, 1000 to 30, 2000 to 35, 4000 to 40, 8000 to 45),
        rightAC = mapOf(250 to 25, 500 to 30, 1000 to 35, 2000 to 30, 4000 to 35, 8000 to 40),
        headphoneId = appleAirpodsId,
        personName = "Jane Smith",
        personAge = 45,
        hasHearingAidExperience = true
    )

    // Fake test 3: Moderate hearing loss with Sony Headphones
    database.localTestQueries.addTestOrIgnore(
        id = "fake-test-003",
        dateTime = kotlin.time.Instant.parse("2025-12-10T09:15:00Z"),
        noiseDuringTest = 20,
        leftAC = mapOf(250 to 45, 500 to 50, 1000 to 55, 2000 to 50, 4000 to 55, 8000 to 60),
        rightAC = mapOf(250 to 50, 500 to 55, 1000 to 50, 2000 to 55, 4000 to 60, 8000 to 65),
        headphoneId = sonyHeadphonesId,
        personName = "Bob Wilson",
        personAge = 62,
        hasHearingAidExperience = true
    )
}

expect fun createSqlDriver(): SqlDriver

