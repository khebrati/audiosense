package ir.khebrati.audiosense.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Calibration
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Results
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDevice
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Setting

@Composable
fun HomeScreen(
    onNavigateSelectDevice: (SelectDevice) -> Unit,
    onNavigateSetting: (Setting) -> Unit,
    onNavigateCalibration: (Calibration) -> Unit,
    onNavigateResult: (Results) -> Unit,
) {
    Column {
        Button(
            onClick = { onNavigateSelectDevice(SelectDevice()) }
        ){
            Text("Start Test")
        }
        Button(
            onClick = { onNavigateSetting(Setting()) }
        ){
            Text("Settings")
        }
        Button(
            onClick = { onNavigateCalibration(Calibration()) }
        ){
            Text("Calibration")
        }
        Button(
            onClick = { onNavigateResult(Results()) }
        ){
            Text("Results")
        }
    }
}
