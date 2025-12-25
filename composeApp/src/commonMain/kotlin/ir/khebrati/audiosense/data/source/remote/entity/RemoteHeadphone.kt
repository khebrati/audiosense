package ir.khebrati.audiosense.data.source.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class RemoteHeadphone(
    val _id: String,
    val name: String,
    val calibrationCoefficients: Map<Int, Pair<Int, Int>> = emptyMap(),
)
