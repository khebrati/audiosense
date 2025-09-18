package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Audiogram(
    leftAC: Map<Int, Int>,
    rightAC: Map<Int, Int>,
    modifier: Modifier = Modifier.size(300.dp),
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            AcousticConstants.allPossibleDbHLs
                .filter { it % 10 == 0 }
                .map { Text(text = it.toString(), style = MaterialTheme.typography.labelSmall) }
        }
        Column(modifier = Modifier.weight(1f)) {
            Spacer(modifier = Modifier.height(8.dp))
            Row { Canvas(modifier = Modifier.height(284.dp).fillMaxWidth()) { AudiogramChart(leftAC, rightAC) } }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun DrawScope.AudiogramChart(leftAC: Map<Int, Int>, rightAC: Map<Int, Int>) {
    AllDbHLOffsets(size).forEach { y ->
        drawLine(color = Color(255, 255, 255), start = Offset(0f, y), end = Offset(size.width, y))
    }
    val areaWidth = (size.width * 6) / 7
    val areaHeight = size.height
    val areaX = (size.width - areaWidth) / 2
    val areaY = 0f
    translate(areaX, areaY) {
        val areaSize = Size(areaWidth, areaHeight)
        println("size is $size")
        println("area size is $areaSize")
        println("Calculating left AC")
        val leftACOffsets = pointOffsets(areaSize, leftAC)
        drawACOffsets(leftACOffsets, SideUiState.LEFT)
        println("Calculating right AC")
        val rightACOffsets = pointOffsets(areaSize, rightAC)
        drawACOffsets(rightACOffsets, SideUiState.RIGHT)
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

private fun DrawScope.drawACOffsets(acOffsets: List<Offset>, side: SideUiState) {
    if (acOffsets.isNotEmpty()) {
        val path =
            Path().apply {
                val firstPoint = acOffsets.first()
                moveTo(firstPoint.x, firstPoint.y)
                if (side == SideUiState.LEFT) drawX(firstPoint) else drawACCircle(firstPoint)
                for (i in 1 until acOffsets.size) {
                    val point = acOffsets[i]
                    lineTo(point.x, point.y)
                    if (side == SideUiState.LEFT) drawX(point) else drawACCircle(point)
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

private fun AllDbHLOffsets(size: Size): List<Float> {
    println("Calculating lines")
    return yOffsets(
        size = size,
        yPoints = AcousticConstants.allPossibleDbHLs.filter { it % 10 == 0 },
    )
}

@Preview
@Composable
fun PreviewAudiogram() {
    Audiogram(
        leftAC = hashMapOf(500 to 20, 1000 to 20, 2000 to 30, 4000 to 55, 8000 to 35),
        rightAC = hashMapOf(500 to 5, 1000 to 0, 2000 to 0, 4000 to 5, 8000 to 5),
    )
}
