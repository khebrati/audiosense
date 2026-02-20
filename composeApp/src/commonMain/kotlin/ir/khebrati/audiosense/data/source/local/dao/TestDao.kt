package ir.khebrati.audiosense.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ir.khebrati.audiosense.data.source.local.entity.LocalTest

@Dao
interface TestDao {
    @Insert
    suspend fun add(test: LocalTest)
    @Query("DELETE FROM LocalTest WHERE id = :id")
    suspend fun deleteById(id: String)
    @Query("DELETE FROM LocalTest")
    suspend fun deleteAll()
}