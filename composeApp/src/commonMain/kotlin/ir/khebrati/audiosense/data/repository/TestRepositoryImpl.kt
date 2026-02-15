@file:OptIn(ExperimentalUuidApi::class)

package ir.khebrati.audiosense.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneNotNull
import ir.khebrati.audiosense.data.toExternalTestList
import ir.khebrati.audiosense.db.AudiosenseDb
import ir.khebrati.audiosense.domain.model.Test
import ir.khebrati.audiosense.domain.repository.TestRepository
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TestRepositoryImpl(
    private val database: AudiosenseDb,
    private val dispatcher: CoroutineDispatcher,
) : TestRepository {
    override suspend fun createTest(
        dateTime: Instant,
        noiseDuringTest: Int,
        leftAC: Map<Int, Int>,
        rightAC: Map<Int, Int>,
        headphoneId: String,
        personName: String?,
        personAge: Int,
        hasHearingAidExperience: Boolean,
    ): String {
        return withContext(dispatcher) {
            val uuid = Uuid.random().toString()
            database.localTestQueries.addTest(
                id = uuid,
                dateTime = dateTime,
                noiseDuringTest = noiseDuringTest.toLong(),
                leftAC = leftAC,
                rightAC = rightAC,
                headphoneId = headphoneId,
                personName = personName,
                personAge = personAge.toLong(),
                hasHearingAidExperience = hasHearingAidExperience,
            )
            uuid
        }
    }

    override suspend fun get(id: String): Test? {
        return withContext(dispatcher) {
            database.localTestQueries.getTestWithHeadphone(id)
                .executeAsOneOrNull()
                ?.toExternalTestList()
        }
    }

    override suspend fun getLastTest(): Test =
        withContext(dispatcher) {
            database.localTestQueries.getAllTestsWithHeadphones()
                .executeAsList()
                .toExternalTestList()
                .maxBy { it.dateTime }
        }

    override suspend fun getAll() =
        withContext(dispatcher) {
            database.localTestQueries.getAllTestsWithHeadphones()
                .executeAsList()
                .toExternalTestList()
        }

    override fun observeAll(): Flow<List<Test>> =
        database.localTestQueries.getAllTestsWithHeadphones()
            .asFlow()
            .mapToList(dispatcher)
            .map { it.toExternalTestList() }

    override fun observe(id: String): Flow<Test> {
        return database.localTestQueries.getTestWithHeadphone(id)
            .asFlow()
            .mapToOneNotNull(dispatcher)
            .map { it.toExternalTestList() }
    }

    override suspend fun deleteById(id: String) {
        withContext(dispatcher) {
            database.localTestQueries.deleteTestById(id)
        }
    }

    override suspend fun deleteAll() {
        withContext(dispatcher) {
            database.localTestQueries.deleteAllTests()
        }
    }
}
