package ir.khebrati.audiosense.domain.useCase.calibrator

import ir.khebrati.audiosense.domain.model.Headphone
import ir.khebrati.audiosense.domain.model.VolumeRecordPerFrequency


interface HeadphoneCalibrator {
    /**
     * Calculates calibration coefficients based on the provided raw data.
     */
    fun calibrate(
        rawAC : Map<Int, Int>,
        calibrationCoefficients: Map<Int, Int>,
    ) : Map<Int,Int>
}
