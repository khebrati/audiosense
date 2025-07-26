package ir.khebrati.audiosense.data.source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.khebrati.audiosense.data.source.entity.LocalHeadphone

@Dao
interface HeadphoneDao {
    @Query("SELECT * FROM LocalHeadphone")
    suspend fun getAll() : List<LocalHeadphone>
    @Insert
    suspend fun add(headphone: LocalHeadphone)
}