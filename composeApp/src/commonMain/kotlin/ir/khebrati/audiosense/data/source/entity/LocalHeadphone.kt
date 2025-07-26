package ir.khebrati.audiosense.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalHeadphone(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val model: String,
    val calibrationCoefficients: Map<Int,Int>,
)
