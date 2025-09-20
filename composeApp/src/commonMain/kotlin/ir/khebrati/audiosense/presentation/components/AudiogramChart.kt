package ir.khebrati.audiosense.presentation.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.presentation.screens.result.SideUiState
import ir.khebrati.audiosense.utils.toSortedMap

fun DrawScope.audiogramChart(leftAC: Map<Int, Int>, rightAC: Map<Int, Int>,hasAudiometricSymbols: Boolean,systemIsDark: Boolean) {
    allDbHLOffsets(size).forEach { y ->
        drawLine(color = if(systemIsDark) Color.White else Color.Black, start = Offset(0f, y), end = Offset(size.width, y), strokeWidth = 2f)
    }
    val areaWidth = (size.width * 6) / 7
    val areaHeight = size.height
    val areaX = (size.width - areaWidth) / 2
    val areaY = 0f
    translate(areaX, areaY) {
        val areaSize = Size(areaWidth, areaHeight)
        val leftACOffsets = pointOffsets(areaSize, leftAC)
        drawACOffsets(leftACOffsets, SideUiState.LEFT,hasAudiometricSymbols)
        val rightACOffsets = pointOffsets(areaSize, rightAC)
        drawACOffsets(rightACOffsets, SideUiState.RIGHT,hasAudiometricSymbols)
    }
}

private fun DrawScope.drawACCircle(point: Offset) {
    drawCircle(
        color = Color.Red,
        radius = 7.dp.toPx(),
        center = Offset(point.x, point.y),
        style = Stroke(),
    )
}

private fun DrawScope.drawACOffsets(acOffsets: List<Offset>, side: SideUiState,hasAudiometricSymbols: Boolean) {
    if (acOffsets.isNotEmpty()) {
        val path =
            Path().apply {
                val firstPoint = acOffsets.first()
                moveTo(firstPoint.x, firstPoint.y)
                if(hasAudiometricSymbols){
                    if (side == SideUiState.LEFT) drawX(firstPoint) else drawACCircle(firstPoint)
                }
                for (i in 1 until acOffsets.size) {
                    val point = acOffsets[i]
                    lineTo(point.x, point.y)
                    if(hasAudiometricSymbols){
                        if (side == SideUiState.LEFT) drawX(point) else drawACCircle(point)
                    }
                }
            }
        val color = if (side == SideUiState.LEFT) Color.Blue else Color.Red
        drawPath(path = path, color = color, style = Stroke(width = 1.dp.toPx()))
    }
}

private fun DrawScope.drawX(firstPoint: Offset) {
    drawLine(
        color = Color.Blue,
        start = Offset(firstPoint.x - 7.dp.toPx(), firstPoint.y - 7.dp.toPx()),
        end = Offset(firstPoint.x + 7.dp.toPx(), firstPoint.y + 7.dp.toPx()),
        strokeWidth = 2.dp.toPx(),
    )
    drawLine(
        color = Color.Blue,
        start = Offset(firstPoint.x + 7.dp.toPx(), firstPoint.y - 7.dp.toPx()),
        end = Offset(firstPoint.x - 7.dp.toPx(), firstPoint.y + 7.dp.toPx()),
        strokeWidth = 2.dp.toPx(),
    )
}

private fun allDbHLOffsets(size: Size): List<Float> {
    return yOffsets(
        size = size,
        yPoints = AcousticConstants.allPossibleDbHLs.filter { it % 10 == 0 },
    )
}

fun pointOffsets(size: Size, points: Map<Int, Int>): List<Offset> {
    val sortedPoints = points.toSortedMap()
    val xOffsets = xOffsets(size, sortedPoints.keys.toList())
    val yOffsets = yOffsets(size, sortedPoints.values.toList())
    return xOffsets.zip(yOffsets).map { pair -> Offset(pair.first, pair.second) }
}

fun yOffsets(size: Size, yPoints: List<Int>): List<Float> {
    val allPossibleOffsets =
        distributePointsUniformInRange(size.height, AcousticConstants.allPossibleDbHLs)
    return yPoints.map { y ->
        val index = AcousticConstants.allPossibleDbHLs.indexOf(y)
        allPossibleOffsets[index]
    }
}

fun xOffsets(size: Size, xPoints: List<Int>): List<Float> {
    val allPossibleOffsets =
        distributePointsUniformInRange(size.width, AcousticConstants.allFrequencyOctaves)
    return xPoints.map { x ->
        val index = AcousticConstants.allFrequencyOctaves.indexOf(x)
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
