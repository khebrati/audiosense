package ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.NoiseMeter
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDevice

@Composable
fun SelectDeviceScreen(
    selectDeviceRoute : SelectDevice,
    onNavigateNoiseMeter: (NoiseMeter) -> Unit,
    onNavigateBack: () -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = selectDeviceRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack
    ) {
        Button(
            onClick = {
                onNavigateNoiseMeter(NoiseMeter)
            }
        ) {
            Text("Select Device")
        }
    }
}
