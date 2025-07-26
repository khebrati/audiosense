package ir.khebrati.audiosense.data.source.dao

import androidx.room.Dao
import androidx.room.Insert
import ir.khebrati.audiosense.data.source.entity.LocalTest

@Dao
interface TestDao {
    @Insert
    fun add(test: LocalTest)
}