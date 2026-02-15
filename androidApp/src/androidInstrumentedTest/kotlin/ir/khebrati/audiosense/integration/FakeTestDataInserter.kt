package ir.khebrati.audiosense.integration

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import ir.khebrati.audiosense.di.initKoin
import ir.khebrati.audiosense.domain.repository.TestRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

/**
 * Instrumented test class for inserting fake LocalTest data for development purposes.
 * These tests use the default pre-populated headphones from the database.
 */
@OptIn(ExperimentalTime::class)
class FakeTestDataInserter : KoinComponent {
    private val testRepo by inject<TestRepository>()

    // Default headphone IDs (pre-populated in the database)
    companion object {
        const val GALAXY_BUDS_FE_ID = "a15c6946-0f18-4ae0-82c1-16a7ef8dc4dc"
        const val APPLE_AIRPODS_ID = "04cad680-777e-41a1-8770-f6bb5ed50ea8"
        const val SONY_HEADPHONES_ID = "9165f20d-1ce6-4eb6-b2a8-0955dd8f6407"
        const val UNCALIBRATED_ID = "1c7b54aa-61cd-487d-ac2f-7a41537a84e8"
    }

    @BeforeTest
    fun setupDi(): Unit = runBlocking {
        stopKoin()
        val context = createContext()
        startAppKoin(context)
    }

    private fun createContext() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun startAppKoin(context: Context) =
        initKoin(nativePlatformModule = module { single<Context> { context } })

    /**
     * Inserts a single fake test with normal hearing thresholds using Galaxy Buds FE.
     */
    @Test
    fun insertFakeTest_normalHearing_galaxyBudsFE() = runTest {
        val dateTime = Clock.System.now()
        val noiseDuringTest = 25
        val leftAC = mapOf(125 to 10, 250 to 10, 500 to 15, 1000 to 10, 2000 to 15, 4000 to 20, 8000 to 20)
        val rightAC = mapOf(125 to 10, 250 to 15, 500 to 10, 1000 to 15, 2000 to 10, 4000 to 15, 8000 to 20)

        val testId = testRepo.createTest(
            dateTime = dateTime,
            noiseDuringTest = noiseDuringTest,
            leftAC = leftAC,
            rightAC = rightAC,
            headphoneId = GALAXY_BUDS_FE_ID,
            personName = "John Doe",
            personAge = 35,
            hasHearingAidExperience = false
        )

        println("Inserted fake test with ID: $testId")
    }

    /**
     * Inserts a fake test with mild hearing loss using Apple Airpods.
     */
    @Test
    fun insertFakeTest_mildHearingLoss_appleAirpods() = runTest {
        val dateTime = Clock.System.now().minus(1.days)
        val noiseDuringTest = 30
        val leftAC = mapOf(125 to 25, 250 to 30, 500 to 35, 1000 to 30, 2000 to 35, 4000 to 40, 8000 to 45)
        val rightAC = mapOf(125 to 20, 250 to 25, 500 to 30, 1000 to 35, 2000 to 30, 4000 to 35, 8000 to 40)

        val testId = testRepo.createTest(
            dateTime = dateTime,
            noiseDuringTest = noiseDuringTest,
            leftAC = leftAC,
            rightAC = rightAC,
            headphoneId = APPLE_AIRPODS_ID,
            personName = "Jane Smith",
            personAge = 45,
            hasHearingAidExperience = true
        )

        println("Inserted fake test with ID: $testId")
    }

    /**
     * Inserts a fake test with moderate hearing loss using Sony Headphones.
     */
    @Test
    fun insertFakeTest_moderateHearingLoss_sonyHeadphones() = runTest {
        val dateTime = Clock.System.now().minus(7.days)
        val noiseDuringTest = 20
        val leftAC = mapOf(125 to 40, 250 to 45, 500 to 50, 1000 to 55, 2000 to 50, 4000 to 55, 8000 to 60)
        val rightAC = mapOf(125 to 45, 250 to 50, 500 to 55, 1000 to 50, 2000 to 55, 4000 to 60, 8000 to 65)

        val testId = testRepo.createTest(
            dateTime = dateTime,
            noiseDuringTest = noiseDuringTest,
            leftAC = leftAC,
            rightAC = rightAC,
            headphoneId = SONY_HEADPHONES_ID,
            personName = "Bob Wilson",
            personAge = 62,
            hasHearingAidExperience = true
        )

        println("Inserted fake test with ID: $testId")
    }

    /**
     * Inserts a fake test with severe hearing loss using uncalibrated headphones.
     */
    @Test
    fun insertFakeTest_severeHearingLoss_uncalibrated() = runTest {
        val dateTime = Clock.System.now().minus(14.days)
        val noiseDuringTest = 15
        val leftAC = mapOf(125 to 70, 250 to 75, 500 to 80, 1000 to 85, 2000 to 80, 4000 to 85, 8000 to 90)
        val rightAC = mapOf(125 to 65, 250 to 70, 500 to 75, 1000 to 80, 2000 to 75, 4000 to 80, 8000 to 85)

        val testId = testRepo.createTest(
            dateTime = dateTime,
            noiseDuringTest = noiseDuringTest,
            leftAC = leftAC,
            rightAC = rightAC,
            headphoneId = UNCALIBRATED_ID,
            personName = "Alice Johnson",
            personAge = 78,
            hasHearingAidExperience = true
        )

        println("Inserted fake test with ID: $testId")
    }

    /**
     * Inserts a fake test without a person name (anonymous test).
     */
    @Test
    fun insertFakeTest_anonymousTest_galaxyBudsFE() = runTest {
        val dateTime = Clock.System.now().minus(3.days)
        val noiseDuringTest = 35
        val leftAC = mapOf(125 to 15, 250 to 20, 500 to 20, 1000 to 25, 2000 to 20, 4000 to 25, 8000 to 30)
        val rightAC = mapOf(125 to 20, 250 to 15, 500 to 20, 1000 to 20, 2000 to 25, 4000 to 20, 8000 to 25)

        val testId = testRepo.createTest(
            dateTime = dateTime,
            noiseDuringTest = noiseDuringTest,
            leftAC = leftAC,
            rightAC = rightAC,
            headphoneId = GALAXY_BUDS_FE_ID,
            personName = null,
            personAge = 28,
            hasHearingAidExperience = false
        )

        println("Inserted anonymous fake test with ID: $testId")
    }

    /**
     * Inserts multiple fake tests at once for comprehensive testing.
     */
    @Test
    fun insertMultipleFakeTests() = runTest {
        val baseTime = Clock.System.now()

        val testConfigs = listOf(
            TestConfig(
                dateOffset = 0.days,
                noise = 25,
                leftAC = mapOf(125 to 10, 250 to 10, 500 to 15, 1000 to 10, 2000 to 15, 4000 to 20, 8000 to 20),
                rightAC = mapOf(125 to 10, 250 to 15, 500 to 10, 1000 to 15, 2000 to 10, 4000 to 15, 8000 to 20),
                headphoneId = GALAXY_BUDS_FE_ID,
                name = "Test User 1",
                age = 25,
                hasExperience = false
            ),
            TestConfig(
                dateOffset = 5.days,
                noise = 30,
                leftAC = mapOf(125 to 30, 250 to 35, 500 to 40, 1000 to 35, 2000 to 40, 4000 to 45, 8000 to 50),
                rightAC = mapOf(125 to 25, 250 to 30, 500 to 35, 1000 to 40, 2000 to 35, 4000 to 40, 8000 to 45),
                headphoneId = APPLE_AIRPODS_ID,
                name = "Test User 2",
                age = 50,
                hasExperience = true
            ),
            TestConfig(
                dateOffset = 10.days,
                noise = 20,
                leftAC = mapOf(125 to 55, 250 to 60, 500 to 65, 1000 to 60, 2000 to 65, 4000 to 70, 8000 to 75),
                rightAC = mapOf(125 to 50, 250 to 55, 500 to 60, 1000 to 65, 2000 to 60, 4000 to 65, 8000 to 70),
                headphoneId = SONY_HEADPHONES_ID,
                name = "Test User 3",
                age = 70,
                hasExperience = true
            )
        )

        testConfigs.forEach { config ->
            val testId = testRepo.createTest(
                dateTime = baseTime.minus(config.dateOffset),
                noiseDuringTest = config.noise,
                leftAC = config.leftAC,
                rightAC = config.rightAC,
                headphoneId = config.headphoneId,
                personName = config.name,
                personAge = config.age,
                hasHearingAidExperience = config.hasExperience
            )
            println("Inserted fake test '${config.name}' with ID: $testId")
        }
    }

    private data class TestConfig(
        val dateOffset: kotlin.time.Duration,
        val noise: Int,
        val leftAC: Map<Int, Int>,
        val rightAC: Map<Int, Int>,
        val headphoneId: String,
        val name: String?,
        val age: Int,
        val hasExperience: Boolean
    )
}

