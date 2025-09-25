package ir.khebrati.audiosense.presentation.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.screens.result.components.Audiogram
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CompactAudiogram(
    modifier: Modifier = Modifier.size(100.dp, 60.dp),
    leftAC: Map<Int, Int>,
    rightAC: Map<Int, Int>,
) {
    Card(
        colors =
            CardDefaults.cardColors()
                .copy(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(width = 0.2.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Audiogram(leftAC, rightAC, modifier)
    }
}

@Preview
@Composable
fun CompactAudiogramPreview() {
    AppTheme {
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
                hashMapOf(
                    125 to 30,
                    250 to 30,
                    500 to 5,
                    1000 to 0,
                    2000 to 0,
                    4000 to 5,
                    8000 to 5,
                ),
        )
    }
}
