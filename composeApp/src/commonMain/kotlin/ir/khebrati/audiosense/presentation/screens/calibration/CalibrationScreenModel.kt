package ir.khebrati.audiosense.presentation.screens.calibration

import androidx.compose.runtime.Immutable

class CalibrationScreenModel {}

@Immutable
data class CalibrationUiState(
    val headphoneModel: String = "",
    val calibrationPerFrequency: Map<Int, VolumeCalibration> =
        listOf(125, 250, 500, 1000, 2000, 4000, 8000).associateWith { VolumeCalibration() },
)
@Immutable
data class VolumeCalibration(
    val playedVolume: Int = 50,
    val measuredVolume: Int = 50,
)
