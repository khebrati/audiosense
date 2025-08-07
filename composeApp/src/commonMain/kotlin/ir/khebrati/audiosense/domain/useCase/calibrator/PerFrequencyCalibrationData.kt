package ir.khebrati.audiosense.domain.useCase.calibrator

data class PerFrequencyCalibrationData(
    val recordedVolumeDbSPL: Int,
    val playedVolumeDbSpl: Int
)
