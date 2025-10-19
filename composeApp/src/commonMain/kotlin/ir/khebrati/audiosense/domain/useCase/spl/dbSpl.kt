package ir.khebrati.audiosense.domain.useCase.spl

import kotlin.math.log10
import kotlin.math.pow

val Number.fromDbSpl : Int get() =20 * log10(this.toFloat()).toInt()
val Number.dbSpl : Float get() =  (10.0f.pow(this.toFloat() / 20f))

//todo implement this.
fun Number.dbHl(freq: Int) = this.dbSpl