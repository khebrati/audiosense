@file:OptIn(ExperimentalTime::class)

package ir.khebrati.audiosense.data.source.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Entity(
    foreignKeys = [ForeignKey(
        entity = LocalHeadphone::class,
        parentColumns = ["id"],
        childColumns = ["headphoneId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocalTest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateTime: Instant,
    val noiseDuringTest: Int,
    val leftAC: Map<Int, Int>,
    val rightAC: Map<Int, Int>,
    val headphoneId: Int
)