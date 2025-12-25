@file:OptIn(ExperimentalTime::class)

package ir.khebrati.audiosense.data.source.local.entity

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
    @PrimaryKey
    val id: String,
    val dateTime: Instant,
    val noiseDuringTest: Int,
    val leftAC: Map<Int, Int>,
    val rightAC: Map<Int, Int>,
    val headphoneId: String,
    val personName: String?,
    val personAge: Int,
    val hasHearingAidExperience: Boolean
)
/**
 * {
 *   "age": 30,
 *   "hearingAidExperience": true,
 *   "date": true,
 *   "leftAC": {
 *     "125": 10,
 *     "250": 15,
 *     "500": 20,
 *     "1000": 25,
 *     "2000": 30,
 *     "4000": 40,
 *     "8000": 50
 *   },
 *   "rightAC": {
 *     "125": 10,
 *     "250": 10,
 *     "500": 15,
 *     "1000": 20,
 *     "2000": 25,
 *     "4000": 35,
 *     "8000": 45
 *   },
 *   "headphoneModel": "64f1a2b3c4d5e6f7a8b9c0d1"
 * }
 */