package ir.khebrati.audiosense.integration

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import ir.khebrati.audiosense.di.initKoin
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class HeadphoneRepositoryIntegrationTest : KoinComponent {
    val headphoneRepo by inject<HeadphoneRepository>()

    @BeforeTest
    fun setupDi() : Unit = runBlocking{
        stopKoin()
        val context = createContext()
        startAppKoin(context)
        headphoneRepo.deleteAll()
    }
    private fun createContext() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun startAppKoin(context: Context) =
        initKoin(nativePlatformModule = module { single<Context> { context } })

    @Test
    fun addAndGetAll_whenHeadphoneAdded_returnsItInList() = runTest {
        // Arrange
        val headphoneName = "Test Headphone"
        val headphoneCalibration = mapOf(1 to 2,5 to 8)

        // Action
        headphoneRepo.createHeadphone(
            calibrationCoefficients = headphoneCalibration,
            model = headphoneName
        )
        val headphones = headphoneRepo.getAll()
        val addedHeadphone = headphones.first()

        // Assert
        assertEquals(1, headphones.size)
        assertEquals(headphoneName, addedHeadphone.model)
        assertEquals(headphoneCalibration, addedHeadphone.calibrationCoefficients)

        // Cleanup
        headphoneRepo.deleteById(addedHeadphone.id)
    }

    @Test
    fun observeAll_whenHeadphoneAdded_emitsUpdatedList() = runTest {
        // Arrange
        val headphoneName = "Flow Test Headphone"
        val headphoneCalibration = mapOf(8 to 2,502 to 8023)

        headphoneRepo.observeAll().test {
            // Assert initial state
            assertTrue(awaitItem().isEmpty(), "Initial list should be empty")

            // Action
            headphoneRepo.createHeadphone(headphoneName, headphoneCalibration)

            // Assert updated state
            val updatedList = awaitItem()
            assertEquals(1, updatedList.size)
            assertEquals(headphoneName, updatedList.first().model)

            // Cleanup
            headphoneRepo.deleteById(updatedList.first().id)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun delete_whenHeadphoneExists_removesItFromStorage() = runTest {
        // Arrange
        headphoneRepo.createHeadphone(
            model = "toBeDeleted",
            calibrationCoefficients = emptyMap()
        )
        val headphoneToDelete = headphoneRepo.getAll().first()

        // Action
        headphoneRepo.deleteById(headphoneToDelete.id)

        // Assert
        val headphonesAfterDelete = headphoneRepo.getAll()
        assertTrue(headphonesAfterDelete.isEmpty())
    }

}