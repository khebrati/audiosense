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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
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
        val minHeight = this.minHeight
        val minDim = min(minHeight, minWidth)
        val isMoreThan100 = minDim > 100.dp
        val hasSymbols = isMoreThan100
        val symbolRadius = minDim / 40
        val hasLabels = isMoreThan100
        val labelFontSize = (minDim / 25).value.sp
        val labelFontWeight = FontWeight((minDim * 2).value.toInt())
        val hasBackgroundLines = isMoreThan100
        val textMeasurer = rememberTextMeasurer()
        Canvas(modifier = modifier.fillMaxWidth()) {
            drawableAudiogram(
                leftAC,
                rightAC,
                systemIsDark = systemIsDark,
                hasAudiometricSymbols = hasSymbols,
                symbolRadius = symbolRadius,
                hasBackgroundLines = hasBackgroundLines,
                backgroundLineStrokeWidth = 2f,
                acStrokeWidth = 5f,
                textMeasurer = textMeasurer,
                hasLabels = hasLabels,
                labelFontSize = labelFontSize,
                labelFontWeight = labelFontWeight
            )
        }
    }
}

fun frequenciesLabels() =
    AcousticConstants.allFrequencyOctaves.map {
        if (it % 1000 == 0) "${it/1000}k" else it.toString()
    }

fun dbHLLabels() = AcousticConstants.allPossibleDbHLs.map { it.toString() }

fun DrawScope.drawableAudiogram(
    leftAC: Map<Int, Int>,
    rightAC: Map<Int, Int>,
    hasAudiometricSymbols: Boolean,
    symbolRadius: Dp,
    systemIsDark: Boolean,
    backgroundLineStrokeWidth: Float,
    acStrokeWidth: Float,
    hasBackgroundLines: Boolean,
    textMeasurer: TextMeasurer,
    hasLabels: Boolean,
    labelFontSize: TextUnit,
    labelFontWeight: FontWeight
) {
    // This is basically the adaptive sizing unit we will use to position anything on x-axis
    // this value is equal to distance between 2 AC points, twice the space for labels, twice
    // the distance between first/last ac points and chart line end.
    val xUnit = size.width / ((AcousticConstants.allFrequencyOctaves.size + 1) * 2)
    val yUnit = size.height / (AcousticConstants.allPossibleDbHLs.size + 4)

    val chartWidth = size.width - 2 * xUnit
    val chartHeight = size.height - 4 * yUnit
    val chartSize = Size(chartWidth, chartHeight)
    val labelColor = if(systemIsDark) Color.White else Color.Black
    dbHlLabels(chartHeight, xUnit, yUnit, textMeasurer, labelFontSize, labelFontWeight,labelColor)
    freqLabels(chartSize, xUnit, yUnit, textMeasurer,labelFontSize,labelFontWeight,labelColor)
    audiogramChart(
        leftAC,
        rightAC,
        xUnit,
        yUnit,
        hasBackgroundLines,
        systemIsDark,
        backgroundLineStrokeWidth,
        hasAudiometricSymbols,
        acStrokeWidth,
        symbolRadius,
        chartSize,
    )
}

private fun DrawScope.audiogramChart(
    leftAC: Map<Int, Int>,
    rightAC: Map<Int, Int>,
    xUnit: Float,
    yUnit: Float,
    hasBackgroundLines: Boolean,
    systemIsDark: Boolean,
    backgroundLineStrokeWidth: Float,
    hasAudiometricSymbols: Boolean,
    acStrokeWidth: Float,
    symbolRadius: Dp,
    chartSize: Size,
) {
    translate(left = xUnit, top = 2 * yUnit) {
        if (hasBackgroundLines) {
            drawChartLines(systemIsDark, backgroundLineStrokeWidth, chartSize)
        }
        val acPaddedAreaSize = Size(chartSize.width - 2 * xUnit,chartSize.height)
        acLines(leftAC, rightAC, hasAudiometricSymbols, acStrokeWidth, symbolRadius, acPaddedAreaSize,xUnit,yUnit)
    }
}

private fun DrawScope.dbHlLabels(
    chartHeight: Float,
    xUnit: Float,
    yUnit: Float,
    textMeasurer: TextMeasurer,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    color: Color
) {
    allDbHLOffsets(chartHeight).forEachIndexed { index, y ->
        val labelResult =
            textMeasurer.measure(
                AnnotatedString(dbHLLabels()[2 * index]),
                style = TextStyle(color = color,fontSize = fontSize, fontWeight = fontWeight)
            )
        val textWidth = labelResult.size.width
        val textHeight = labelResult.size.height
        drawText(
            topLeft = Offset(xUnit / 2 - (textWidth / 2), 2 * yUnit + y - textHeight / 2),
            textLayoutResult = labelResult,
        )
    }
}

private fun DrawScope.freqLabels(
    chartSize: Size,
    xUnit: Float,
    yUnit: Float,
    textMeasurer: TextMeasurer,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    color: Color
) {
    val acPaddedAreaWidth = chartSize.width - 2 * xUnit
    allFreqOffsets(acPaddedAreaWidth).forEachIndexed { index, x ->
        val labelResult =
            textMeasurer.measure(
                AnnotatedString(frequenciesLabels()[index]),
                style = TextStyle(color = color,fontSize = fontSize, fontWeight = fontWeight),
            )
        val textWidth = labelResult.size.width
        drawText(
            topLeft =
                Offset(2 * xUnit + x - (textWidth / 2f), size.height - yUnit),
            textLayoutResult = labelResult,
        )
    }
}

private fun DrawScope.drawChartLines(
    systemIsDark: Boolean,
    backgroundLineStrokeWidth: Float,
    size: Size,
) {
    allDbHLOffsets(size.height).forEach { y ->
        drawLine(
            color = if (systemIsDark) Color.White else Color.Black,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = backgroundLineStrokeWidth,
        )
    }
}

private fun DrawScope.acLines(
    leftAC: Map<Int, Int>,
    rightAC: Map<Int, Int>,
    hasAudiometricSymbols: Boolean,
    acStrokeWidth: Float,
    symbolRadius: Dp,
    size: Size,
    xUnit: Float,
    yUnit: Float,
) {
    val areaX = xUnit
    val areaY = 0f
    translate(areaX, areaY) {
        val leftACOffsets = acOffsets(size, leftAC)
        if (leftACOffsets.isNotEmpty()) {
            drawACOffsets(
                acOffsets = leftACOffsets,
                side = SideUiState.LEFT,
                hasAudiometricSymbols = hasAudiometricSymbols,
                strokeWidth = acStrokeWidth,
                symbolRadius = symbolRadius,
            )
        }
        val rightACOffsets = acOffsets(size, rightAC)
        if (rightACOffsets.isNotEmpty()) {
            drawACOffsets(
                acOffsets = rightACOffsets,
                side = SideUiState.RIGHT,
                hasAudiometricSymbols = hasAudiometricSymbols,
                strokeWidth = acStrokeWidth,
                symbolRadius = symbolRadius,
            )
        }
    }
}

private fun DrawScope.drawACOffsets(
    acOffsets: List<Offset>,
    side: SideUiState,
    hasAudiometricSymbols: Boolean,
    strokeWidth: Float,
    symbolRadius: Dp,
) {
    val color = if (side == SideUiState.LEFT) Color.Blue else Color.Red
    val drawSymbol =
        if (side == SideUiState.LEFT)
            { offset: Offset, radius: Dp -> drawXSymbol(offset, radius, color) }
        else { offset: Offset, radius: Dp -> drawCircle(offset, radius, color) }
    val path =
        Path().apply {
            val firstPoint = acOffsets.first()
            moveTo(firstPoint.x, firstPoint.y)
            if (hasAudiometricSymbols) {
                drawSymbol(firstPoint, symbolRadius)
            }
            for (i in 1 until acOffsets.size) {
                val point = acOffsets[i]
                lineTo(point.x, point.y)
                if (hasAudiometricSymbols) {
                    drawSymbol(point, symbolRadius)
                }
            }
        }
    drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
}

private fun DrawScope.drawXSymbol(offset: Offset, radius: Dp, color: Color) {
    drawLine(
        color = color,
        start = Offset(offset.x - radius.toPx(), offset.y - radius.toPx()),
        end = Offset(offset.x + radius.toPx(), offset.y + radius.toPx()),
        strokeWidth = (radius / 6).toPx(),
    )
    drawLine(
        color = color,
        start = Offset(offset.x + radius.toPx(), offset.y - radius.toPx()),
        end = Offset(offset.x - radius.toPx(), offset.y + radius.toPx()),
        strokeWidth = (radius / 6).toPx(),
    )
}

private fun DrawScope.drawCircle(offset: Offset, radius: Dp, color: Color) {
    drawCircle(
        color = color,
        radius = radius.toPx(),
        center = Offset(offset.x, offset.y),
        style = Stroke(width = (radius / 6).toPx()),
    )
}

private fun allDbHLOffsets(height: Float): List<Float> {
    return dbHLVerticalDistances(
        height = height,
        yPoints = AcousticConstants.allPossibleDbHLs.filter { it % 10 == 0 },
    )
}

private fun allFreqOffsets(width: Float): List<Float> {
    return frequencyHorizontalDistances(
        width = width,
        xPoints = AcousticConstants.allFrequencyOctaves,
    )
}

private fun acOffsets(size: Size, points: Map<Int, Int>): List<Offset> {
    val sortedPoints = points.toSortedMap()
    val xOffsets = frequencyHorizontalDistances(size.width, sortedPoints.keys.toList())
    val yOffsets = dbHLVerticalDistances(size.height, sortedPoints.values.toList())
    return xOffsets.zip(yOffsets).map { pair -> Offset(pair.first, pair.second) }
}

private fun dbHLVerticalDistances(height: Float, yPoints: List<Int>): List<Float> {
    val allPossibleOffsets =
        distributePointsUniformInRange(height, AcousticConstants.allPossibleDbHLs)
    return yPoints.map { y ->
        val index = AcousticConstants.allPossibleDbHLs.indexOf(y)
        allPossibleOffsets[index]
    }
}

private fun frequencyHorizontalDistances(width: Float, xPoints: List<Int>): List<Float> {
    val allPossibleOffsets =
        distributePointsUniformInRange(width, AcousticConstants.allFrequencyOctaves)
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
private fun distributePointsUniformInRange(rangeSize: Float, points: List<Int>) =
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
