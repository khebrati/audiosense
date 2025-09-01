package ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Earbuds
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.NoiseMeter
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDevice
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SelectDeviceScreen(
    selectDeviceRoute: SelectDevice,
    onNavigateNoiseMeter: (NoiseMeter) -> Unit,
    onNavigateBack: () -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = selectDeviceRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack,
    ) {
        Button(onClick = { onNavigateNoiseMeter(NoiseMeter) }) { Text("Select Device") }
    }
}

@Composable
fun HeadphoneItem(modelName: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(12.dp),
    ) {
        Box(
            modifier =
                Modifier.background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium,
                    )
                    .size(50.dp),
            contentAlignment = Alignment.Center,
        ) {
            val icon = if (isEarbuds(modelName)) Icons.Default.Earbuds else Icons.Default.Headphones
            Icon(contentDescription = "Headphone icon", imageVector = icon)
        }
        Text(text = modelName)
    }
}

private fun isEarbuds(name: String) = name.lowercase().contains(Regex("bud|buds|airpod|airpods"))

@Preview(showBackground = true)
@Composable
fun HeadphoneItemPreview() {
    AppTheme { HeadphoneItem(modelName = "Galaxy Buds", modifier = Modifier.fillMaxWidth()) }
}
