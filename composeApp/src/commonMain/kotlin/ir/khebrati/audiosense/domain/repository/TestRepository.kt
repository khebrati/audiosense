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
        headphoneId: String,
        personName: String?,
        personAge: Int,
        hasHearingAidExperience: Boolean
    ) : String

    suspend fun getAll(): List<Test>
    suspend fun get(id: String) : Test?
    suspend fun getLastTest(): Test
    fun observeAll(): Flow<List<Test>>
    fun observe(id: String) : Flow<Test>
    suspend fun deleteById(id: String)

    suspend fun deleteAll()
}