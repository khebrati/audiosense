package ir.khebrati.audiosense.domain.repository

import ir.khebrati.audiosense.domain.model.Test
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface TestRepository {
    suspend fun createTest(
        dateTime: Instant,
        noiseDuringTest: Int,
        leftAC: Map<Int, Int>,
        rightAC: Map<Int, Int>,
        headphoneId: String
    ) : String

    suspend fun getAll(): List<Test>
    fun observeAll(): Flow<List<Test>>
    suspend fun deleteById(id: String)

    suspend fun deleteAll()
}