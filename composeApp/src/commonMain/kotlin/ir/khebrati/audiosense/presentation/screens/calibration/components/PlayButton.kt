package ir.khebrati.audiosense.presentation.screens.calibration.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlayButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(modifier = modifier.fillMaxWidth().height(60.dp), onClick = onClick) {
        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
        Spacer(modifier = Modifier.width(4.dp))
        Text("PLAY")
    }
}

@Preview
@Composable
internal fun PlayButtonPreview() {
    AppTheme { PlayButton(modifier = Modifier, onClick = {}) }
}
