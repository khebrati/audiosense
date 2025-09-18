package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import ir.khebrati.audiosense.domain.model.AcousticConstants

fun pointOffsets(size: Size, points: Map<Int, Int>): List<Offset> {
    val xOffsets = xOffsets(size,points.keys.toList())
    val yValues = yOffsets(size, points.values.toList())
    return xOffsets.zip(yValues).map { pair -> Offset(pair.first, pair.second) }
}

fun yOffsets(size: Size, yPoints: List<Int>) : List<Float>{
    val allPossibleOffsets = distributePointsUniformInRange(size.height, AcousticConstants.allPossibleDbHLs)
    return yPoints.map { y ->
        val index = AcousticConstants.allPossibleDbHLs.indexOf(y)
        allPossibleOffsets[index]
    }
}

fun xOffsets(size: Size, xPoints: List<Int>) : List<Float>{
    val allPossibleOffsets = distributePointsUniformInRange(size.width, AcousticConstants.allFrequencyOctaves)
    return xPoints.map { y ->
        val index = AcousticConstants.allFrequencyOctaves.indexOf(y)
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
