package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
        yOffsets(
            size = size,
            yPoints = AcousticConstants.allPossibleDbHLs
        )
            .forEach { y ->
                drawLine(
                    color = Color(255, 255, 255),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                )
            }
        val leftACOffsets = pointOffsets(size, leftAC)
        if (leftACOffsets.isNotEmpty()) {
            val leftACPath =
                Path().apply {
                    val firstPoint = leftACOffsets.first()
                    moveTo(firstPoint.x, firstPoint.y)
                    for (i in 1 until leftACOffsets.size) {
                        val point = leftACOffsets[i]
                        lineTo(point.x, point.y)
                    }
                }
            drawPath(path = leftACPath, color = Color.Blue, style = Stroke(width = 3.dp.toPx()))
        }
    }
}

@Preview
@Composable
fun previewAudiogram() {
    Audiogram(leftAC = hashMapOf(125 to 40, 1000 to 0,500 to 70), rightAC = emptyMap())
}
