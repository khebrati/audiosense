package ir.khebrati.audiosense.domain.useCase.calibrator

import ir.khebrati.audiosense.domain.model.VolumeRecordPerFrequency

interface HeadphoneCalibrator {
    /**
     * Calculates calibration coefficients based on the provided raw data.
     */
    fun calibrate(
        rawData : Map<Int, VolumeRecordPerFrequency>
    ) : Map<Int,Int>
}
