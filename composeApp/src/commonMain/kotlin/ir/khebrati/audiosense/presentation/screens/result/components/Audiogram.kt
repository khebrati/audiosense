package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.domain.model.AcousticConstants
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Audiogram(
    leftAC: Map<Int, Int>,
    rightAC: Map<Int, Int>,
    modifier: Modifier = Modifier.size(250.dp),
) {
    Canvas(modifier = modifier) {
        println("size is $size")
        AllDbHLOffsets().forEach { y ->
            drawLine(
                color = Color(255, 255, 255),
                start = Offset(0f, y),
                end = Offset(size.width, y),
            )
        }
        println("Calculating left AC")
        val leftACOffsets = pointOffsets(size, leftAC)
        if (leftACOffsets.isNotEmpty()) {
            val leftACPath =
                Path().apply {
                    val firstPoint = leftACOffsets.first()
                    moveTo(firstPoint.x, firstPoint.y)
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
                    for (i in 1 until leftACOffsets.size) {
                        val point = leftACOffsets[i]
                        lineTo(point.x, point.y)
                        drawLine(
                            color = Color.Blue,
                            start = Offset(point.x - 7.dp.toPx(), point.y - 7.dp.toPx()),
                            end = Offset(point.x + 7.dp.toPx(), point.y + 7.dp.toPx()),
                            strokeWidth = 2.dp.toPx(),
                        )
                        drawLine(
                            color = Color.Blue,
                            start = Offset(point.x + 7.dp.toPx(), point.y - 7.dp.toPx()),
                            end = Offset(point.x - 7.dp.toPx(), point.y + 7.dp.toPx()),
                            strokeWidth = 2.dp.toPx(),
                        )
                    }
                }
            drawPath(path = leftACPath, color = Color.Blue, style = Stroke(width = 1.dp.toPx()))
        }
        println("Calculating right AC")
        val rightACOffsets = pointOffsets(size, rightAC)
        if (rightACOffsets.isNotEmpty()) {
            val rightACPath =
                Path().apply {
                    val firstPoint = rightACOffsets.first()
                    drawCircle(
                        color = Color.Red, // Choose your desired color
                        radius = 7.dp.toPx(), // Choose your desired radius
                        center = Offset(firstPoint.x, firstPoint.y),
                        style = Stroke(),
                    )
                    moveTo(firstPoint.x, firstPoint.y)
                    for (i in 1 until rightACOffsets.size) {
                        val point = rightACOffsets[i]
                        lineTo(point.x, point.y)
                        drawCircle(
                            color = Color.Red, // Choose your desired color
                            radius = 7.dp.toPx(), // Choose your desired radius
                            center = Offset(point.x, point.y),
                            style = Stroke(),
                        )
                    }
                }
            drawPath(path = rightACPath, color = Color.Red, style = Stroke(width = 1.dp.toPx()))
        }
    }
}

private fun DrawScope.AllDbHLOffsets(): List<Float> {
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
        leftAC = hashMapOf(500 to 20, 1000 to 20, 2000 to 30, 4000 to 55),
        rightAC = hashMapOf(500 to 5, 1000 to 0, 2000 to 0, 4000 to 5, 8000 to 5),
    )
}
