package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.presentation.components.audiogramChart
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
        val labelFontWeight = FontWeight((minDim * 3/2).value.toInt())
        val hasBackgroundLines = isMoreThan100
        Row {
            if (hasLabels) {
                Column(modifier = Modifier.fillMaxHeight().padding(horizontal = 3.dp)) {
                    Spacer(modifier = Modifier.height(minHeight/50))
                    Column(
                        modifier = Modifier.weight(80f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        AcousticConstants.allPossibleDbHLs
                            .filter { it % 10 == 0 }
                            .map {
                                Text(
                                    text = it.toString(),
                                    fontWeight = labelFontWeight,
                                    fontSize = labelFontSize,
                                )
                            }
                    }
                    Spacer(modifier = Modifier.height(minHeight / 15))
                }
            }
            Column {
                if(hasLabels) Spacer(modifier = Modifier.height(minHeight / 25))
                Canvas(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    audiogramChart(
                        leftAC,
                        rightAC,
                        systemIsDark = systemIsDark,
                        hasAudiometricSymbols = hasSymbols,
                        symbolRadius = symbolRadius,
                        hasBackgroundLines = hasBackgroundLines,
                        backgroundLineStrokeWidth = 2f,
                        acStrokeWidth = 5f
                    )
                }
                if (hasLabels) {
                    Spacer(modifier = Modifier.height(minHeight / 22))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        frequenciesLabels().map {
                            Box(
                                modifier = Modifier.weight(2f),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = it,
                                    fontWeight = labelFontWeight ,
                                    fontSize = labelFontSize,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun frequenciesLabels() =
    AcousticConstants.allFrequencyOctaves.map {
        if (it % 1000 == 0) "${it/1000}k" else it.toString()
    }

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
