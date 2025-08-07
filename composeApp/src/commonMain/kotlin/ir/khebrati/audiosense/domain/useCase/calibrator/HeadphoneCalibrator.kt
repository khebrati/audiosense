package ir.khebrati.audiosense.domain.useCase.calibrator

interface HeadphoneCalibrator {
    /**
     * Calculates calibration coefficients based on the provided raw data.
     */
    fun calibrate(
        rawData : Map<Int, PerFrequencyCalibrationData>
    ) : Map<Int,Int>
}
