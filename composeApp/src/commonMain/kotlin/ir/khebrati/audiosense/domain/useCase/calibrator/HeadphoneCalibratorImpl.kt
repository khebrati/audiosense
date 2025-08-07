package ir.khebrati.audiosense.domain.useCase.calibrator

class HeadphoneCalibratorImpl : HeadphoneCalibrator {
    /**
     * Calculates calibration coefficients based on the provided raw data.
     */
    override fun calibrate(rawData: Map<Int, PerFrequencyCalibrationData>): Map<Int, Int> {
        //TODO do calibration
        return emptyMap()
    }
}