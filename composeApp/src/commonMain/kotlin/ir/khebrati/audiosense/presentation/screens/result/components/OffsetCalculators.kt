package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import ir.khebrati.audiosense.domain.model.AcousticConstants
import kotlin.collections.sort

fun pointOffsets(size: Size, points: Map<Int, Int>): List<Offset> {
    val sortedPoints = points.toSortedMap()
    println("keys are ${sortedPoints.keys}")
    val xOffsets = xOffsets(size,sortedPoints.keys.toList())
    println("Y offsets are $xOffsets")
    println("values are ${sortedPoints.values.toList()}")
    val yOffsets = yOffsets(size, sortedPoints.values.toList())
    println("Y offsets are $yOffsets")
    return xOffsets.zip(yOffsets).map { pair -> Offset(pair.first, pair.second) }
}

fun yOffsets(size: Size, yPoints: List<Int>) : List<Float>{
    val allPossibleOffsets = distributePointsUniformInRange(size.height, AcousticConstants.allPossibleDbHLs)
    return yPoints.map { y ->
        val index = AcousticConstants.allPossibleDbHLs.indexOf(y)
        println("given y $y maps to index $index so offset ${allPossibleOffsets[index]}")
        allPossibleOffsets[index]
    }
}
fun <K : Comparable<K>, V> Map<out K, V>.toSortedMap(): Map<K, V>{
    val orderedKeys = this.keys.sorted()
    val mappedValue = orderedKeys.map { this[it] }
    return orderedKeys.zip(mappedValue).toMap() as Map<K, V>
}

fun xOffsets(size: Size, xPoints: List<Int>) : List<Float>{
    val allPossibleOffsets = distributePointsUniformInRange(size.width, AcousticConstants.allFrequencyOctaves)
    return xPoints.map { x ->
        val index = AcousticConstants.allFrequencyOctaves.indexOf(x)
        println("given x $x maps to index $index so offset ${allPossibleOffsets[index]}")
        allPossibleOffsets[index]
    }
}


/**
 * Distributes some points evenly in a range of custom size and returns their position.
 *
 * listOf(0,1,2,3,4,5)
 *
 * range: ------------
 *
 * rangeSize = 10cm
 *
 * distributes: 0---1---2---3---4---5
 *
 * returns: listOf(0,2cm,4cm,6cm,8cm,10cm)
 */
fun distributePointsUniformInRange(rangeSize: Float, points: List<Int>) =
    points.mapIndexed { index, value -> (rangeSize * index) / (points.size - 1) }
