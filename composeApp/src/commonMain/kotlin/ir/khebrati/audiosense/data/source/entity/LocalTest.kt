package ir.khebrati.audiosense.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalTest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val noise: Int,
    val dateTime: String,
)