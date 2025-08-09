package ir.khebrati.audiosense.domain.useCase.spl

import kotlin.math.log10
import kotlin.math.pow

fun Number.dbSpl() = 10 * log10(this.toInt() / (20 * 10e-6)).toInt()
fun Number.fromDbSpl() = (10.0f.pow(this.toFloat() / 10f) / (20 * 10e-6)).toFloat()
