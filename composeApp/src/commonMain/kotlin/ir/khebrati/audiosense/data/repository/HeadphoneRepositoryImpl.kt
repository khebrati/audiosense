@file:OptIn(ExperimentalUuidApi::class)

package ir.khebrati.audiosense.data.repository

import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.data.source.local.dao.HeadphoneDao
import ir.khebrati.audiosense.data.source.local.entity.LocalHeadphone
import ir.khebrati.audiosense.data.source.remote.HeadphoneFetcher
import ir.khebrati.audiosense.data.toExternal
import ir.khebrati.audiosense.data.toLocal
import ir.khebrati.audiosense.domain.model.VolumeRecordPerFrequency
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HeadphoneRepositoryImpl(
    private val headphoneDao: HeadphoneDao,
    private val headphoneFetcher: HeadphoneFetcher,
    private val dispatcher: CoroutineDispatcher,
) : HeadphoneRepository {
    override suspend fun createHeadphone(
        model: String,
        calibrationCoefficients: Map<Int, VolumeRecordPerFrequency>,
    ) : String{
        return withContext(dispatcher) {
            val uuid = withContext(dispatcher) {
                Uuid.random().toString()
            }
            val localHeadphone = LocalHeadphone(
                id = uuid,
                model = model,
                calibrationCoefficients = calibrationCoefficients.toLocal()
            )
            headphoneDao.add(localHeadphone)
            uuid
        }
    }

    override suspend fun getAll() = withContext(dispatcher) {
        headphoneDao.getAll().toExternal()
    }

    override fun observeAll() = headphoneDao.observeAll()
        .map { withContext(dispatcher) { it.toExternal() } }
        .onStart {
            // Trigger background refresh from server
            // Room will automatically emit the new data when the DB is updated
            CoroutineScope(dispatcher).launch {
                refreshFromServer()
            }
        }

    private suspend fun refreshFromServer() {
        try {
            val remoteHeadphones = headphoneFetcher.fetchAllFromServer()
            if (remoteHeadphones.isNotEmpty()) {
                Logger.d { "Fetched ${remoteHeadphones.size} headphones from server" }
                val localHeadphones = remoteHeadphones.toLocal()
                headphoneDao.upsertAll(localHeadphones)
            }
        } catch (e: Exception) {
            Logger.e(e) { "Failed to refresh headphones from server" }
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