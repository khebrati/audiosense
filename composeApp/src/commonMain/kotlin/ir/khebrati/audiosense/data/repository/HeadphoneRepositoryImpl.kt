@file:OptIn(ExperimentalUuidApi::class)

package ir.khebrati.audiosense.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.data.source.remote.HeadphoneFetcher
import ir.khebrati.audiosense.data.toExternal
import ir.khebrati.audiosense.data.toExternalTestList
import ir.khebrati.audiosense.data.toLocal
import ir.khebrati.audiosense.db.AudiosenseDb
import ir.khebrati.audiosense.domain.model.Headphone
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
    private val database: AudiosenseDb,
    private val headphoneFetcher: HeadphoneFetcher,
    private val dispatcher: CoroutineDispatcher,
) : HeadphoneRepository {
    override suspend fun createHeadphone(
        model: String,
        calibrationCoefficients: Map<Int, VolumeRecordPerFrequency>,
    ) : String{
        return withContext(dispatcher) {
            val uuid = Uuid.random().toString()
            database.localHeadphoneQueries.addHeadphone(
                id = uuid,
                model = model,
                calibrationCoefficients = calibrationCoefficients.toLocal()
            )
            uuid
        }
    }

    override suspend fun getById(id: String): Headphone? = withContext(dispatcher){
        database.localHeadphoneQueries.getHeadphoneById(id).executeAsOneOrNull()?.toExternalTestList()
    }

    override suspend fun getAll() = withContext(dispatcher) {
        database.localHeadphoneQueries.getAllHeadphones().executeAsList().toExternal()
    }

    override fun observeAll(): Flow<List<Headphone>> =
        database.localHeadphoneQueries.getAllHeadphones()
            .asFlow()
            .mapToList(dispatcher)
            .map { it.toExternal() }
            .onStart {
                // Trigger background refresh from server
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
                localHeadphones.forEach { headphone ->
                    database.localHeadphoneQueries.upsertHeadphone(
                        id = headphone.id,
                        model = headphone.model,
                        calibrationCoefficients = headphone.calibrationCoefficients
                    )
                }
            }
        } catch (e: Exception) {
            Logger.e(e) { "Failed to refresh headphones from server" }
        }
    }

    override suspend fun deleteById(id: String) : Unit= withContext(dispatcher) {
        database.localHeadphoneQueries.deleteHeadphoneById(id)
    }

    override suspend fun deleteAll() {
        withContext(dispatcher){
            database.localHeadphoneQueries.deleteAllHeadphones()
        }
    }
}