package ir.khebrati.audiosense.presentation.screens.calibration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Calibration
import ir.khebrati.audiosense.presentation.screens.calibration.components.DeviceNameCard
import ir.khebrati.audiosense.presentation.screens.calibration.components.FrequencyCard
import ir.khebrati.audiosense.presentation.screens.calibration.components.PlayButton
import ir.khebrati.audiosense.presentation.screens.calibration.components.SaveFAB
import ir.khebrati.audiosense.presentation.screens.calibration.components.VolumeCard
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.collections.listOf

@Preview
@Composable
fun CalibrationScreenContentPreview() {
    val frequency = remember { mutableStateOf(1000) }
    val frequencies = listOf(125, 250, 500, 1000, 2000, 4000, 8000)
    val volume = remember { mutableStateOf(50) }
    val deviceName = remember { mutableStateOf("") }
    CalibrationScreenContent(
        onDeviceNameChange = {
            deviceName.value = it
        },
        onFrequencyChange = {
            frequency.value = frequencies[it.toInt()]
        },
        frequency = frequency.value,
        volume = volume.value,
        onVolumeChange = {
            volume.value = it
        },
        deviceName = deviceName.value,
        frequencies = frequencies,
        onSaveClick = {},
        onPlayClick = {}
    )
}

@Composable
fun CalibrationScreen(
    calibrationRoute: Calibration,
    onNavigateBack: () -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = calibrationRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack,
    ){
        CalibrationScreenContentPreview()
    }
}

@Composable
fun CalibrationScreenContent(
    onDeviceNameChange: (String) -> Unit,
    deviceName: String = "",
    onFrequencyChange: (Float) -> Unit,
    frequencies: List<Int>,
    frequency: Int,
    volume: Int,
    onVolumeChange: (Int) -> Unit,
    onPlayClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppTheme {
        Column(
            modifier = modifier.padding(top = 80.dp, start = 25.dp, end = 25.dp, bottom = 25.dp)
                .fillMaxHeight()
        ) {
            DeviceNameCard(
                onValueChange = onDeviceNameChange,
                value = deviceName,
            )
            Spacer(modifier = Modifier.height(30.dp))
            FrequencyCard(
                frequency = frequency,
                frequencies = frequencies,
                onFreqChange = onFrequencyChange,
            )
            Spacer(modifier = Modifier.height(30.dp))
            VolumeCard(
                volume = volume, onVolumeChange = onVolumeChange,
            )
            Spacer(modifier = Modifier.height(30.dp))
            PlayButton(onClick = onPlayClick)
            Spacer(modifier = Modifier.weight(1f))
            SaveFAB(
                onClick = onSaveClick
            )
        }
    }
}



