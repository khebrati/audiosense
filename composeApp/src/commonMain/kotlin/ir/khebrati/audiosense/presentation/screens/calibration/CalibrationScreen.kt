package ir.khebrati.audiosense.presentation.screens.calibration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Calibration
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.PlaySound
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.Save
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetFrequency
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetMeasuredVolumeForCurrentFrequency
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetVolumeToPlayForCurrentFrequency
import ir.khebrati.audiosense.presentation.screens.calibration.components.DeviceNameInputCard
import ir.khebrati.audiosense.presentation.screens.calibration.components.FrequencyCard
import ir.khebrati.audiosense.presentation.screens.calibration.components.MeasureVolumeCard
import ir.khebrati.audiosense.presentation.screens.calibration.components.PlayButton
import ir.khebrati.audiosense.presentation.screens.calibration.components.PlayVolumeCard
import ir.khebrati.audiosense.presentation.screens.calibration.components.SaveFAB
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinNavViewModel

@Preview
@Composable
fun CalibrationScreenContentPreview() {
    AppTheme { CalibrationScreen(calibrationRoute = Calibration, onNavigateBack = {}) }
}

@Composable
fun CalibrationScreen(
    calibrationRoute: Calibration,
    onNavigateBack: () -> Unit,
    viewModel: CalibrationViewModel = koinNavViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var headphoneModel by remember { mutableStateOf("") }
    var isHeadphoneModelTextFieldError by remember { mutableStateOf(false) }
    fun handleSaveClick() {
        if (headphoneModel.isBlank()) {
            isHeadphoneModelTextFieldError = true
        }
        else {
            viewModel.onUiAction(Save(headphoneModel))
            onNavigateBack()
        }
    }
    AudiosenseScaffold(
        screenTitle = calibrationRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack,
        floatingActionButton = { SaveFAB(onClick = ::handleSaveClick) },
    ) {
        CalibrationScreenContent(
            state = uiState,
            onUiAction = viewModel::onUiAction,
            headphoneModel = headphoneModel,
            onHeadphoneModelChange = { headphoneModel = it },
            isHeadphoneModelTextFieldError = isHeadphoneModelTextFieldError,
        )
    }
}

@Composable()
fun CalibrationScreenContent(
    state: CalibrationUiState,
    onUiAction: (CalibrationUiAction) -> Unit,
    headphoneModel: String = "",
    onHeadphoneModelChange: (String) -> Unit,
    isHeadphoneModelTextFieldError: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DeviceNameInputCard(onValueChange = onHeadphoneModelChange, value = headphoneModel, isError = isHeadphoneModelTextFieldError)
        Spacer(modifier = Modifier.height(25.dp))
        FrequencyCard(
            frequency = state.frequency,
            frequencies = state.frequencies,
            onFreqChange = { onUiAction(SetFrequency(it)) },
        )
        Spacer(modifier = Modifier.height(25.dp))
        PlayVolumeCard(
            volume = state.volumeData.volumeToPlayDbSpl,
            onVolumeChange = { onUiAction(SetVolumeToPlayForCurrentFrequency(it)) },
        )
        Spacer(modifier = Modifier.height(25.dp))
        MeasureVolumeCard(
            volume = state.volumeData.measuredVolumeDbSpl,
            onVolumeChange = { onUiAction(SetMeasuredVolumeForCurrentFrequency(it)) },
        )
        Spacer(modifier = Modifier.height(25.dp))
        PlayButton(onClick = { onUiAction(PlaySound) })
    }
}
