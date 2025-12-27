package ir.khebrati.audiosense.data.source.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class RemoteAudiogram(
    val age: Int,
    val hearingAidExperience: Boolean,
    val date: String,
    val leftAC: Map<String, Int>,
    val rightAC: Map<String, Int>,
    val headphoneModel: String,
)

