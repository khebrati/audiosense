package ir.khebrati.audiosense.presentation.screens.testPreparation.noiseMeter

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.NoiseMeter
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Test

@Composable
fun NoiseMeterScreen(
    noiseMeterRoute : NoiseMeter,
    onNavigateTest: (Test) -> Unit,
) {
    Button(
        onClick = {
            onNavigateTest(Test())
        }
    ){
        Text("Get noise")
    }
}