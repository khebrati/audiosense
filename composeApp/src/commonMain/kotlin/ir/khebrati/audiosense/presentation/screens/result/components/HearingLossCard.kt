package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.screens.result.SideUiState
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HearingLossCard(lossDbHl: Int, side: SideUiState, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.size(185.dp),
        colors =
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                val icon =
                    if (side == SideUiState.LEFT) Icons.Default.Close else Icons.Outlined.Circle
                val red = Color(255, 0, 0)
                val blue = Color(0, 112, 192)
                val color = if (side == SideUiState.LEFT) blue else red
                Icon(
                    imageVector = icon,
                    contentDescription = "sideIcon",
                    tint = color,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    side.toString(),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        lossDbHl.toString(),
                        style = MaterialTheme.typography.displayLargeEmphasized,
                    )
                    Text("dBHL", style = MaterialTheme.typography.labelSmall)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(determineLossLevel(lossDbHl), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

private fun determineLossLevel(loss: Int) =
    when {
        loss < 20 -> "Normal"
        20 <= loss && loss < 40 -> "Mild Loss"
        40 <= loss && loss < 70 -> "Moderate Loss"
        70 <= loss && loss < 90 -> "Severe Loss"
        else -> "Profound Loss"
    }

@Preview
@Composable
fun previewHearingLossCardLeft() {
    AppTheme { HearingLossCard(lossDbHl = 70, side = SideUiState.LEFT) }
}

@Preview
@Composable
fun previewHearingLossCardRight() {
    AppTheme { HearingLossCard(lossDbHl = 70, side = SideUiState.RIGHT) }
}
