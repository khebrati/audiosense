package ir.khebrati.audiosense.domain.useCase.calibrator


class HeadphoneCalibratorImpl : HeadphoneCalibrator {
    /**
     * Calculates calibration coefficients based on the provided raw data.
     */
    override fun calibrate(
        rawAC: Map<Int, Int>,
        calibrationCoefficients: Map<Int, Int>
    ): Map<Int, Int> {
        TODO("Not yet implemented")
    }
}