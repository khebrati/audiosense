package ir.khebrati.audiosense.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalHeadphone(
    @PrimaryKey
    val id : String,
    val model: String,
    val calibrationCoefficients: Map<Int,Int>,
)
