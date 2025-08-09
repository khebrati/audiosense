package ir.khebrati.audiosense.presentation.screens.calibration.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VolumeCard(
    title: @Composable () -> Unit = {},
    volume: Int,
    onVolumeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().height(30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                title()
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(25.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onVolumeChange((volume - 5)) },
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(Icons.Default.Remove, "Decrease Volume")
                }
                Text(
                    text = volume.toString(),
                    style = MaterialTheme.typography.displayLargeEmphasized,
                    color = MaterialTheme.colorScheme.primary,
                )
                IconButton(
                    onClick = { onVolumeChange((volume + 5)) },
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(Icons.Default.Add, "Increase Volume")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun PlayVolumeCard(volume: Int, onVolumeChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    VolumeCard(
        title = { Text("PLAY VOLUME (dB)", style = MaterialTheme.typography.titleMedium) },
        volume = volume,
        onVolumeChange = onVolumeChange,
        modifier = modifier,
    )
}

@Composable
fun MeasureVolumeCard(volume: Int, onVolumeChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    VolumeCard(
        title = { Text("MEASURED VOLUME (dB)", style = MaterialTheme.typography.titleMedium) },
        volume = volume,
        onVolumeChange = onVolumeChange,
        modifier = modifier,
    )
}

@Preview
@Composable
fun VolumeCardPreview() {
    AppTheme { VolumeCard(volume = 50, modifier = Modifier.height(150.dp), onVolumeChange = {}) }
}
