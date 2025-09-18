package ir.khebrati.audiosense.domain.model

object AcousticConstants {
    val allFrequencyOctaves = listOf(125, 250,500, 1000, 2000, 4000, 8000)
    val allPossibleDbHLs = listOf(-10,-5,0,5,10,15,20,25,30,45,40,45,50,55,60,65,70,75,80,85,90)
    const val MAX_PCM_16BIT_VALUE = Short.MAX_VALUE // 2^15 - 1
    const val MIN_PCM_16BIT_VALUE = Short.MIN_VALUE // -2^15
    /** 10 * log([MAX_PCM_16BIT_VALUE], 10.0) (rounded down to 90) */
    const val MAX_DB_SPL = 90
    const val MIN_DB_SPL = 0
}
