package ir.khebrati.audiosense.integration

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import ir.khebrati.audiosense.di.initKoin
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import ir.khebrati.audiosense.domain.repository.TestRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class TestHeadphoneRepositoryIntegrationTest : KoinComponent {
    val testRepo by inject<TestRepository>()
    val headphoneRepo by inject<HeadphoneRepository>()

    @BeforeTest
    fun setupDi(): Unit = runBlocking {
        stopKoin()
        val context = createContext()
        startAppKoin(context)
        testRepo.deleteAll()
    }

    private fun createContext() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun startAppKoin(context: Context) =
        initKoin(nativePlatformModule = module { single<Context> { context } })

    @Test
    fun addAndGetAll_whenTestAdded_returnsItInList() = runTest {
        // Arrange
        val dateTime = Instant.DISTANT_PAST
        val noiseDuringTest = 10
        val leftAC = mapOf(250 to 20, 500 to 25)
        val rightAC = mapOf(250 to 15, 500 to 30)
        val testHeadphone = headphoneRepo.createHeadphone(
            model = "TestModel",
            calibrationCoefficients = emptyMap()
        )
        val headphoneId = testHeadphone

        // Action
        testRepo.createTest(dateTime, noiseDuringTest, leftAC, rightAC, headphoneId,null,0,false)
        val tests = testRepo.getAll()
        val addedTest = tests.first()

        // Assert
        assertEquals(1, tests.size)
        assertEquals(dateTime, addedTest.dateTime)
        assertEquals(noiseDuringTest, addedTest.noiseDuringTest)
        assertEquals(leftAC, addedTest.leftAC)
        assertEquals(rightAC, addedTest.rightAC)
        assertEquals(headphoneId, addedTest.headphone.id)

        // Cleanup
        testRepo.deleteById(addedTest.id)
    }

    @Test
    fun observeAll_whenTestAdded_emitsUpdatedList() = runTest {
        // Arrange
        val dateTime = Instant.DISTANT_FUTURE
        val noiseDuringTest = 5
        val leftAC = mapOf(1000 to 10)
        val rightAC = mapOf(1000 to 15)
        val testHeadphone = headphoneRepo.createHeadphone(
            model = "TestModel",
            calibrationCoefficients = emptyMap()
        )
        val headphoneId = testHeadphone

        testRepo.observeAll().test {
            // Assert initial state
            assertTrue(awaitItem().isEmpty(), "Initial list should be empty")

            // Action
            testRepo.createTest(dateTime, noiseDuringTest, leftAC, rightAC, headphoneId,null,0,false)

            // Assert updated state
            val updatedList = awaitItem()
            assertEquals(1, updatedList.size)
            assertEquals(dateTime, updatedList.first().dateTime)

            // Cleanup
            testRepo.deleteById(updatedList.first().id)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun delete_whenTestExists_removesItFromStorage() = runTest {
        // Arrange
        val testHeadphone = headphoneRepo.createHeadphone(
            model = "TestModel",
            calibrationCoefficients = emptyMap()
        )
        val headphoneId = testHeadphone
        testRepo.createTest(
            dateTime = Instant.DISTANT_PAST,
            noiseDuringTest = 0,
            leftAC = emptyMap(),
            rightAC = emptyMap(),
            headphoneId =headphoneId,
            personAge = 0,
            personName = null,
            hasHearingAidExperience = false
        )
        val testToDelete = testRepo.getAll().first()

        // Action
        testRepo.deleteById(testToDelete.id)

        // Assert
        val testsAfterDelete = testRepo.getAll()
        assertTrue(testsAfterDelete.isEmpty())
    }

    @Test
    fun getAll_whenTestsRelatedToHeadphonesAreCreated_returnsTestsRelatedWithHeadphones() = runTest{
        // Arrange
        val headphoneId1 = headphoneRepo.createHeadphone(
            model = "Headphone1",
            calibrationCoefficients = emptyMap()
        )
        val headphoneId2 = headphoneRepo.createHeadphone(
            model = "Headphone2",
            calibrationCoefficients = emptyMap()
        )

        testRepo.createTest(
            dateTime = Instant.DISTANT_PAST,
            noiseDuringTest = 0,
            leftAC = emptyMap(),
            rightAC = emptyMap(),
            headphoneId = headphoneId1,
                    personAge = 0,
            personName = null,
            hasHearingAidExperience = false
        )

        testRepo.createTest(
            dateTime = Instant.DISTANT_PAST,
            noiseDuringTest = 0,
            leftAC = emptyMap(),
            rightAC = emptyMap(),
            headphoneId = headphoneId2,
            personAge = 0,
            personName = null,
            hasHearingAidExperience = false
        )

        // Action
        val tests = testRepo.getAll()

        // Assert
        assertEquals(2, tests.size)
        assertTrue(tests.any { it.headphone.id == headphoneId1 })
        assertTrue(tests.any { it.headphone.id == headphoneId2 })
    }

}