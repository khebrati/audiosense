package ir.khebrati.audiosense.presentation.screens.setup.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

// To get scroll offset
val PagerState.pageOffset: Float
    get() = this.currentPage + this.currentPageOffsetFraction


fun DrawScope.drawIndicator(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    radius: CornerRadius,
    color: Color,
) {
    val rect = RoundRect(x, y - height / 2, x + width, y + height / 2, radius)
    val path = Path().apply { addRoundRect(rect) }
    drawPath(path, color)
}

@Composable
fun Indicator(
    totalWidth: Dp,
    activeLineWidth: Dp,
    circleSpacing: Dp,
    width: Dp,
    height: Dp,
    pagerState: PagerState,
    radius: CornerRadius,
    activeLineColor: Color,
    notActiveLineColor: Color,
) {
    Canvas(modifier = Modifier.width(width = totalWidth).height(height + 6.dp).padding(3.dp)) {
        val spacing = circleSpacing.toPx()
        val dotWidth = width.toPx()
        val dotHeight = height.toPx()

        val activeDotWidth = activeLineWidth.toPx()
        var x = 0f
        val y = center.y

        repeat(pagerState.pageCount) { i ->
            val posOffset = pagerState.pageOffset
            val dotOffset = posOffset % 1
            val current = posOffset.toInt()

            val factor = (dotOffset * (activeDotWidth - dotWidth))

            val calculatedWidth =
                when {
                    i == current -> activeDotWidth - factor
                    i - 1 == current || (i == 0 && posOffset > pagerState.pageCount - 1) -> dotWidth + factor
                    else -> dotWidth
                }

            val color = if(i == current) activeLineColor else notActiveLineColor
            drawIndicator(x, y, calculatedWidth, dotHeight, radius, color)
            x += calculatedWidth + spacing
        }
    }
}

@Preview
@Composable
fun PreviewIndicator() {
    AppTheme {
        val pagerState = rememberPagerState { 3 }
        Indicator(
            totalWidth = 50.dp,
            activeLineWidth = 15.dp,
            circleSpacing = 3.dp,
            width = 5.dp,
            height = 5.dp,
            pagerState = pagerState,
            radius = CornerRadius(100),
            activeLineColor = MaterialTheme.colorScheme.primary,
            notActiveLineColor = MaterialTheme.colorScheme.surfaceDim
        )
    }
}
