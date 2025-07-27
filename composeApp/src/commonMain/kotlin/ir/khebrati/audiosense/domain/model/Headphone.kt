package ir.khebrati.audiosense.domain.model

data class Headphone(
    val id : String,
    val model: String,
    val calibrationCoefficients: Map<Int,Int>,
)
