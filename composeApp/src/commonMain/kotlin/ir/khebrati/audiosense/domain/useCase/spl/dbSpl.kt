package ir.khebrati.audiosense.domain.useCase.spl

import ir.khebrati.audiosense.domain.model.AcousticConstants
import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.pow


fun findClosestInList(target: Double,list: List<Int>) : Int{
    var closestIndex = 0;
    var leastDiff = Double.MAX_VALUE;
    list.forEachIndexed { index,value ->
        val diff = (value - target).absoluteValue
        if((diff < leastDiff)){
            leastDiff = diff
            closestIndex = index
        }
    }
    return list[closestIndex]
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
