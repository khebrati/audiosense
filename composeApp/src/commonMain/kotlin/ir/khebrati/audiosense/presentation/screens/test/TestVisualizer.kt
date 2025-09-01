package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import ir.khebrati.audiosense.presentation.theme.AppTheme
import kotlin.math.min
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RotatingSmoothStar(modifier: Modifier = Modifier, numVerticesPerRadius: Int, color: Color, fullRotationTimeMillis: Int) {
    BoxWithConstraints(modifier = modifier) {
        val width = maxWidth.px()
        val height = maxHeight.px()
        val minDimension = min(width,height)
        val radius = minDimension / 2
        val innerRadius = radius / 2
        val smoothStar =
            RoundedPolygon.star(
                radius = radius,
                innerRadius = innerRadius,
                numVerticesPerRadius = numVerticesPerRadius,
                rounding = CornerRounding(innerRadius / 2, smoothing = 1f),
                centerX = width / 2,
                centerY = height / 2,
            )
        val path = smoothStar.toPath()
        val infiniteTransition = rememberInfiniteTransition("Animate Rotation")
        val rotationProgress = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(fullRotationTimeMillis, easing = LinearEasing)
            )
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            rotate(
                degrees = rotationProgress.value
            ){
                drawPath(path, color = color, style = Stroke(width = 3.dp.toPx()))
            }
        }
    }
}

@Composable fun Dp.px() = with(LocalDensity.current) { toPx() }

@Composable
fun AnimatedTestVisualizer(
    color: Color,
    modifier: Modifier
){
    RotatingSmoothStar(modifier = modifier,4, color.copy(alpha = 0.8f),50000)
    RotatingSmoothStar(modifier = modifier,5,color.copy(alpha = 0.5f),90000,)
    RotatingSmoothStar(modifier = modifier,3,color.copy(alpha = 0.3f),70000)
}

@Preview(showBackground = true)
@Composable
fun PreviewTestVisualizer() {
    AppTheme {
        val color = MaterialTheme.colorScheme.primary
        AnimatedTestVisualizer(color = color, modifier = Modifier.fillMaxSize())
    }
}
