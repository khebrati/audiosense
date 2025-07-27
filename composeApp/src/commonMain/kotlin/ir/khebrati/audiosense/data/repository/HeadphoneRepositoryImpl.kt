@file:OptIn(ExperimentalUuidApi::class)

package ir.khebrati.audiosense.data.repository

import ir.khebrati.audiosense.data.source.local.dao.HeadphoneDao
import ir.khebrati.audiosense.data.source.local.dao.TestHeadphoneDao
import ir.khebrati.audiosense.data.source.local.entity.LocalHeadphone
import ir.khebrati.audiosense.data.source.local.entity.LocalTest
import ir.khebrati.audiosense.data.toExternal
import ir.khebrati.audiosense.domain.model.Headphone
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HeadphoneRepositoryImpl(
    private val headphoneDao: HeadphoneDao,
    private val dispatcher: CoroutineDispatcher,
) : HeadphoneRepository {
    override suspend fun createHeadphone(
        model: String,
        calibrationCoefficients: Map<Int, Int>,
    ) : String{
        return withContext(dispatcher) {
            val uuid = withContext(dispatcher) {
                Uuid.random().toString()
            }
            val localHeadphone = LocalHeadphone(
                id = uuid,
                model = model,
                calibrationCoefficients = calibrationCoefficients
            )
            headphoneDao.add(localHeadphone)
            uuid
        }
    }

    override suspend fun getAll() = withContext(dispatcher) {
        headphoneDao.getAll().toExternal()
    }

    override fun observeAll() = headphoneDao.observeAll().map {
        withContext(dispatcher) {
            it.toExternal()
        }
    }

    override suspend fun deleteById(id: String) = withContext(dispatcher) {
        headphoneDao.deleteById(id)
    }

    override suspend fun deleteAll() {
        withContext(dispatcher){
            headphoneDao.deleteAll()
        }
    }
}