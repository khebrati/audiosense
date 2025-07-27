@file:OptIn(ExperimentalUuidApi::class)

package ir.khebrati.audiosense.data.repository

import ir.khebrati.audiosense.data.source.local.dao.TestDao
import ir.khebrati.audiosense.data.source.local.dao.TestHeadphoneDao
import ir.khebrati.audiosense.data.source.local.entity.LocalTest
import ir.khebrati.audiosense.data.toExternal
import ir.khebrati.audiosense.domain.model.Test
import ir.khebrati.audiosense.domain.repository.TestRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TestRepositoryImpl(
    private val testDao: TestDao,
    private val testHeadphoneDao: TestHeadphoneDao,
    private val dispatcher: CoroutineDispatcher,
) : TestRepository {
    override suspend fun createTest(
        dateTime: Instant,
        noiseDuringTest: Int,
        leftAC: Map<Int, Int>,
        rightAC: Map<Int, Int>,
        headphoneId: String
    ) : String{
        return withContext(dispatcher){
            val uuid = withContext(dispatcher) {
                Uuid.random().toString()
            }
            val localTest = LocalTest(
                id = uuid,
                dateTime = dateTime,
                noiseDuringTest = noiseDuringTest,
                leftAC = leftAC,
                rightAC = rightAC,
                headphoneId = headphoneId
            )
            testDao.add(localTest)
            uuid
        }
    }

    override suspend fun getAll() = withContext(dispatcher) {
        testHeadphoneDao.getAll().toExternal()
    }

    override fun observeAll(): Flow<List<Test>>  =
        testHeadphoneDao.observeAll().map {
            withContext(dispatcher){
                it.toExternal()
            }
        }

    override suspend fun deleteById(id: String) {
        withContext(dispatcher){
            testDao.deleteById(id)
        }
    }

    override suspend fun deleteAll() {
        withContext(dispatcher){
            testDao.deleteAll()
        }
    }
}