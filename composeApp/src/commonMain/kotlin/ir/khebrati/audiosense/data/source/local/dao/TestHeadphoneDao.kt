package ir.khebrati.audiosense.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import ir.khebrati.audiosense.data.source.local.entity.LocalHeadphone
import ir.khebrati.audiosense.data.source.local.entity.LocalTest
import kotlinx.coroutines.flow.Flow

@Dao
interface TestHeadphoneDao {
    @Query("SELECT * FROM LocalHeadphone JOIN LocalTest ON LocalTest.headphoneId = LocalHeadphone.id")
    suspend fun getAll() : Map<LocalHeadphone,List<LocalTest>>
    @Query("SELECT * FROM LocalHeadphone JOIN LocalTest ON LocalTest.headphoneId = LocalHeadphone.id")
    fun observeAll() : Flow<Map<LocalHeadphone, List<LocalTest>>>
}