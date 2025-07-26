package ir.khebrati.audiosense.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class LocalTestResult(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val side: String,
    val AC: String,
)