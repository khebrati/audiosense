package ir.khebrati.audiosense.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import ir.khebrati.audiosense.data.source.entity.LocalHeadphone
import ir.khebrati.audiosense.data.source.entity.LocalTest

@Dao
interface TestHeadphoneDao {
    @Query("SELECT * FROM LocalHeadphone JOIN LocalTest ON LocalTest.headphoneId = LocalHeadphone.id")
    suspend fun getAll() : Map<LocalHeadphone,List<LocalTest>>
}