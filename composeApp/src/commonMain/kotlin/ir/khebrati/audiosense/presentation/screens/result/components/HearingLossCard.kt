package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.screens.result.SideUiState
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HearingLossCard(lossDbHl: Int, side: SideUiState, modifier: Modifier = Modifier) {
    Card(modifier = modifier.size(185.dp)) {
        Column(modifier = modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(side.toString(), style = MaterialTheme.typography.labelSmall)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(lossDbHl.toString(), style = MaterialTheme.typography.displayLargeEmphasized)
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
fun previewHearingLossCard() {
    AppTheme { HearingLossCard(lossDbHl = 70, side = SideUiState.RIGHT) }
}
