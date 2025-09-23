package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.presentation.screens.result.SideUiState
import ir.khebrati.audiosense.utils.toSortedMap
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Audiogram(
    leftAC: Map<Int, Int>,
    rightAC: Map<Int, Int>,
    modifier: Modifier = Modifier.size(300.dp),
) {
    val systemIsDark = isSystemInDarkTheme()
    BoxWithConstraints(modifier) {
        println("max height: $maxHeight")
        println("min height: $minHeight")
        println("max width: $maxWidth")
        println("min width: $minWidth")
        val minHeight = this.minHeight
        val minDim = min(minHeight, minWidth)
        val isMoreThan100 = minDim > 100.dp
        val hasSymbols = isMoreThan100
        val symbolRadius = minDim / 40
        val hasLabels = isMoreThan100
        val labelFontSize = (minDim / 30).value.sp
        val labelFontWeight = FontWeight((minDim * 3 / 2).value.toInt())
        val hasBackgroundLines = isMoreThan100
        val textMeasurer = rememberTextMeasurer()
        Canvas(modifier = modifier.fillMaxWidth()) {
            audiogramChart(
                leftAC,
                rightAC,
                systemIsDark = systemIsDark,
                hasAudiometricSymbols = hasSymbols,
                symbolRadius = symbolRadius,
                hasBackgroundLines = hasBackgroundLines,
                backgroundLineStrokeWidth = 2f,
                acStrokeWidth = 5f,
                textMeasurer = textMeasurer,
            )
        }
    }
}

fun frequenciesLabels() =
    AcousticConstants.allFrequencyOctaves.map {
        if (it % 1000 == 0) "${it/1000}k" else it.toString()
    }

fun dbHLLabels() =
    AcousticConstants.allPossibleDbHLs.map {it.toString()}

fun DrawScope.audiogramChart(
    leftAC: Map<Int, Int>,
    rightAC: Map<Int, Int>,
    hasAudiometricSymbols: Boolean,
    symbolRadius: Dp,
    systemIsDark: Boolean,
    backgroundLineStrokeWidth: Float,
    acStrokeWidth: Float,
    hasBackgroundLines: Boolean,
    textMeasurer: TextMeasurer,
) {
    val areaWidth = (size.width * 14) / 16
    val areaHeight = (size.height * 20 ) / 26
    val areaX = (size.width - areaWidth) / 2
    val areaY = (size.height - areaHeight) / 2
    val chartWithoutLabelsSize = Size(
        width = areaWidth,
        height = areaHeight
    )
    allDbHLOffsets(chartWithoutLabelsSize).forEachIndexed { index,y ->
        val labelResult =
            textMeasurer.measure(AnnotatedString(dbHLLabels()[index]), style = TextStyle(fontSize = 8.sp))
        val textWidth = labelResult.size.width
        val textHeight = labelResult.size.height
        drawText(topLeft = Offset((size.width / 32) - (textWidth/2), areaY + y - textHeight/2), textLayoutResult = labelResult)
    }
    FreqLabels(size, textMeasurer)
    translate(
        left = areaX,
        top = areaY
    ){
        LinesArea(hasBackgroundLines, textMeasurer, systemIsDark, backgroundLineStrokeWidth,chartWithoutLabelsSize)
        ACArea(leftAC, hasAudiometricSymbols, acStrokeWidth, symbolRadius, rightAC,chartWithoutLabelsSize)
    }
}

private fun DrawScope.FreqLabels(size: Size, textMeasurer: TextMeasurer) {
    val areaWidth = (size.width * 12) / 16
    val areaHeight = size.height
    val areaX = ((size.width - areaWidth) / 2)
    val areaY = 0f
    val acPaddedAreaSize = Size(areaWidth, areaHeight)
    translate(areaX, areaY) {
        allFreqOffsets(acPaddedAreaSize).forEachIndexed { index,x ->
            val labelResult =
                textMeasurer.measure(AnnotatedString(frequenciesLabels()[index]), style = TextStyle(fontSize = 8.sp))
            val textWidth = labelResult.size.width
            val textHeight = labelResult.size.height
            drawText(topLeft = Offset(x - textWidth / 2f, size.height - 3*(size.height/26)/2 - textHeight/2), textLayoutResult = labelResult)
        }
    }
}

private fun DrawScope.LinesArea(
    hasBackgroundLines: Boolean,
    textMeasurer: TextMeasurer,
    systemIsDark: Boolean,
    backgroundLineStrokeWidth: Float,
    size: Size
) {
    if (hasBackgroundLines)
        allDbHLOffsets(size).forEach { y ->
            drawLine(
                color = if (systemIsDark) Color.White else Color.Black,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = backgroundLineStrokeWidth,
            )
        }
}

private fun DrawScope.ACArea(
    leftAC: Map<Int, Int>,
    hasAudiometricSymbols: Boolean,
    acStrokeWidth: Float,
    symbolRadius: Dp,
    rightAC: Map<Int, Int>,
    size: Size
) {
    val areaWidth = (size.width * 14) / 16
    val areaHeight = size.height
    val areaX = (size.width - areaWidth) / 2
    val areaY = 0f
    val acPaddedAreaSize = Size(areaWidth, areaHeight)
    translate(areaX, areaY) {
        val leftACOffsets = pointOffsets(acPaddedAreaSize, leftAC)
        drawACOffsets(
            acOffsets = leftACOffsets,
            side = SideUiState.LEFT,
            hasAudiometricSymbols = hasAudiometricSymbols,
            strokeWidth = acStrokeWidth,
            symbolRadius = symbolRadius,
        )
        val rightACOffsets = pointOffsets(acPaddedAreaSize, rightAC)
        this.drawACOffsets(
            acOffsets = rightACOffsets,
            side = SideUiState.RIGHT,
            hasAudiometricSymbols = hasAudiometricSymbols,
            strokeWidth = acStrokeWidth,
            symbolRadius = symbolRadius,
        )
    }
}

private fun DrawScope.drawACCircle(point: Offset, radius: Dp) {
    drawCircle(
        color = Color.Red,
        radius = radius.toPx(),
        center = Offset(point.x, point.y),
        style = Stroke(width = (radius / 6).toPx()),
    )
}

private fun DrawScope.drawACOffsets(
    acOffsets: List<Offset>,
    side: SideUiState,
    hasAudiometricSymbols: Boolean,
    strokeWidth: Float,
    symbolRadius: Dp,
) {
    if (acOffsets.isNotEmpty()) {
        val path =
            Path().apply {
                val firstPoint = acOffsets.first()
                moveTo(firstPoint.x, firstPoint.y)
                if (hasAudiometricSymbols) {
                    if (side == SideUiState.LEFT) drawX(firstPoint, symbolRadius)
                    else drawACCircle(firstPoint, symbolRadius)
                }
                for (i in 1 until acOffsets.size) {
                    val point = acOffsets[i]
                    lineTo(point.x, point.y)
                    if (hasAudiometricSymbols) {
                        if (side == SideUiState.LEFT) drawX(point, symbolRadius)
                        else drawACCircle(point, symbolRadius)
                    }
                }
            }
        val color = if (side == SideUiState.LEFT) Color.Blue else Color.Red
        drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
    }
}

private fun DrawScope.drawX(firstPoint: Offset, xSymbolRadius: Dp) {
    drawLine(
        color = Color.Blue,
        start = Offset(firstPoint.x - xSymbolRadius.toPx(), firstPoint.y - xSymbolRadius.toPx()),
        end = Offset(firstPoint.x + xSymbolRadius.toPx(), firstPoint.y + xSymbolRadius.toPx()),
        strokeWidth = (xSymbolRadius / 6).toPx(),
    )
    drawLine(
        color = Color.Blue,
        start = Offset(firstPoint.x + xSymbolRadius.toPx(), firstPoint.y - xSymbolRadius.toPx()),
        end = Offset(firstPoint.x - xSymbolRadius.toPx(), firstPoint.y + xSymbolRadius.toPx()),
        strokeWidth = (xSymbolRadius / 6).toPx(),
    )
}

private fun allDbHLOffsets(size: Size): List<Float> {
    return yOffsets(
        size = size,
        yPoints = AcousticConstants.allPossibleDbHLs.filter { it % 10 == 0 },
    )
}

private fun allFreqOffsets(size: Size): List<Float> {
    return xOffsets(
        size = size,
        xPoints = AcousticConstants.allFrequencyOctaves,
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

@Preview
@Composable
fun PreviewAudiogramNormalSize() {
    Audiogram(
        modifier = Modifier.size(300.dp),
        leftAC =
            hashMapOf(
                125 to 90,
                250 to 50,
                500 to 20,
                1000 to 20,
                2000 to 30,
                4000 to 55,
                8000 to 35,
            ),
        rightAC =
            hashMapOf(125 to 30, 250 to 30, 500 to 5, 1000 to 0, 2000 to 0, 4000 to 5, 8000 to 5),
    )
}

@Preview
@Composable
fun PreviewAudiogramHuge() {
    Audiogram(
        modifier = Modifier.size(700.dp),
        leftAC =
            hashMapOf(
                125 to 90,
                250 to 50,
                500 to 20,
                1000 to 20,
                2000 to 30,
                4000 to 55,
                8000 to 35,
            ),
        rightAC =
            hashMapOf(125 to 30, 250 to 30, 500 to 5, 1000 to 0, 2000 to 0, 4000 to 5, 8000 to 5),
    )
}

@Preview
@Composable
fun PreviewAudiogramSmall() {
    Audiogram(
        modifier = Modifier.size(150.dp),
        leftAC =
            hashMapOf(
                125 to 90,
                250 to 50,
                500 to 20,
                1000 to 20,
                2000 to 30,
                4000 to 55,
                8000 to 35,
            ),
        rightAC =
            hashMapOf(125 to 30, 250 to 30, 500 to 5, 1000 to 0, 2000 to 0, 4000 to 5, 8000 to 5),
    )
}

@Preview
@Composable
fun PreviewAudiogramCompact() {
    Audiogram(
        modifier = Modifier.size(100.dp),
        leftAC =
            hashMapOf(
                125 to 90,
                250 to 50,
                500 to 20,
                1000 to 20,
                2000 to 30,
                4000 to 55,
                8000 to 35,
            ),
        rightAC =
            hashMapOf(125 to 30, 250 to 30, 500 to 5, 1000 to 0, 2000 to 0, 4000 to 5, 8000 to 5),
    )
}

@Preview
@Composable
fun PhonePreview() {
    Audiogram(
        modifier = Modifier.height(300.dp).width(342.dp),
        leftAC =
            hashMapOf(
                125 to 90,
                250 to 50,
                500 to 20,
                1000 to 20,
                2000 to 30,
                4000 to 55,
                8000 to 35,
            ),
        rightAC =
            hashMapOf(125 to 30, 250 to 30, 500 to 5, 1000 to 0, 2000 to 0, 4000 to 5, 8000 to 5),
    )
}
