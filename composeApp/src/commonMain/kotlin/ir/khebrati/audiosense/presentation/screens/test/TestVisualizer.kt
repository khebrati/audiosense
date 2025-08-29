package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.min

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TestVisualizer(modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val width = maxWidth.px()
        val height = maxHeight.px()
        val minDimension = min(width,height)
        val radius = minDimension / 2
        val innerRadius = radius / 2
        val rounderStar =
            RoundedPolygon.star(
                radius = radius,
                innerRadius = innerRadius,
                numVerticesPerRadius = 4,
                rounding = CornerRounding(innerRadius, smoothing = 1f),
                centerX = width / 2,
                centerY = height / 2,
            )
        val path = rounderStar.toPath()
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(
                path,
                color = Color.Gray,
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}

@Composable fun Dp.px() = with(LocalDensity.current) { toPx() }

@Preview(showBackground = true)
@Composable
fun PreviewTestVisualizer() {
    TestVisualizer(modifier = Modifier.fillMaxSize())
}
