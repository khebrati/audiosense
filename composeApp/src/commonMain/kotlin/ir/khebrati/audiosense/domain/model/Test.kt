package ir.khebrati.audiosense.domain.model

import kotlin.time.Instant

data class Test(
    val id: String,
    val dateTime: Instant,
    val noiseDuringTest: Int,
    val leftAC: Map<Int, Int>,
    val rightAC: Map<Int, Int>,
    val headphone : Headphone
)