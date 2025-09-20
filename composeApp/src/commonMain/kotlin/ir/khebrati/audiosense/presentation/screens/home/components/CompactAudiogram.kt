package ir.khebrati.audiosense.presentation.screens.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.components.audiogramChart
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CompactAudiogram(
    modifier: Modifier = Modifier.size(100.dp, 60.dp),
    leftAC: Map<Int,Int>,
    rightAC: Map<Int,Int>
){
    val systemIsDark = isSystemInDarkTheme()
    Canvas(modifier = modifier){
//        audiogramChart(leftAC,rightAC,false,systemIsDark)
    }
}

@Preview
@Composable
fun CompactAudiogramPreview(){
    CompactAudiogram(
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