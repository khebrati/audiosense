package ir.khebrati.audiosense.presentation.screens.testPreparation.noiseMeter

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.NoiseMeter
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Test

@Composable
fun NoiseMeterScreen(
    noiseMeterRoute : NoiseMeter,
    onNavigateTest: (Test) -> Unit,
    onNavigateBack: () -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = noiseMeterRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack
,
    ) {
        Button(
            onClick = {
                onNavigateTest(Test)
            }
        ) {
            Text("Get noise")
        }
    }
}