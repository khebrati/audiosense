package ir.khebrati.audiosense.data.source.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class RemoteTest(
    val age: Int,
    val hearingAidExperience: Boolean,
    val date: String,
    val leftAC: Map<String, Int>,
    val rightAC: Map<String, Int>,
    val headphoneModel: String,
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

