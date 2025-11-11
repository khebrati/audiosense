package ir.khebrati.audiosense.domain.useCase.spl

import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

fun mapToDbHl(audiogramList: Map<Int, Int>) =
    audiogramList.mapValues { (key, value) -> value.dbHl(key) }

fun Number.dbHl(freq: Int): Int {
    val diff =
        normalEarHearingThresholds[freq]
            ?: throw IllegalArgumentException(
                "Given freq $freq does not have an equivalent db hl threshold"
            )
    return (this.dbSpl + diff).roundToInt()
}

private val normalEarHearingThresholds =
    mapOf(
        125 to 22.1,
        250 to 11.4,
        500 to 4.4,
        1000 to 2.4,
        2000 to -1.3,
        4000 to -5.4,
        8000 to 12.6,
    )

val Number.fromDbSpl: Int
    get() = 20 * log10(this.toFloat()).toInt()
val Number.dbSpl: Float
    get() = (10.0f.pow(this.toFloat() / 20f))
