package ir.khebrati.audiosense.domain.repository

import ir.khebrati.audiosense.domain.model.Headphone
import kotlinx.coroutines.flow.Flow

interface HeadphoneRepository {
    suspend fun createHeadphone(
        model: String,
        calibrationCoefficients: Map<Int, Int>,
    ) : String

    suspend fun getAll(): List<Headphone>
    fun observeAll(): Flow<List<Headphone>>
    suspend fun deleteById(id: String)
    suspend fun deleteAll()
}