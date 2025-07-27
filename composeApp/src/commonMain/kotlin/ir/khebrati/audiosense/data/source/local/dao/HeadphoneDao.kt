package ir.khebrati.audiosense.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ir.khebrati.audiosense.data.source.local.entity.LocalHeadphone
import kotlinx.coroutines.flow.Flow

@Dao
interface HeadphoneDao {
    @Query("SELECT * FROM LocalHeadphone")
    suspend fun getAll(): List<LocalHeadphone>

    @Query("SELECT * FROM LocalHeadphone")
    fun observeAll(): Flow<List<LocalHeadphone>>

    @Insert
    suspend fun add(headphone: LocalHeadphone)

    @Query("DELETE FROM LocalHeadphone WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM LocalHeadphone")
    suspend fun deleteAll()
}