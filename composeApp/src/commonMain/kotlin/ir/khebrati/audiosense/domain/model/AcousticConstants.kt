package ir.khebrati.audiosense.domain.model

object AcousticConstants {
    val frequencyOctaves = listOf(125, 250,500, 1000, 2000, 4000, 8000)
    const val MAX_PCM_16BIT_VALUE = Short.MAX_VALUE // 2^15 - 1
    const val MIN_PCM_16BIT_VALUE = Short.MIN_VALUE // -2^15
    /** 10 * log([MAX_PCM_16BIT_VALUE], 10.0) (rounded down to 90) */
    const val MAX_DB_SPL = 90
    const val MIN_DB_SPL = 0
}
