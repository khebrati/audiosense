package ir.khebrati.audiosense.domain.useCase.spl

import kotlin.math.log10
import kotlin.math.pow

val Number.dbSpl : Int get() =20 * log10(this.toFloat()).toInt()
fun Number.fromDbSpl() = (10.0f.pow(this.toFloat() / 20f))
