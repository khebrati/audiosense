package ir.khebrati.audiosense.domain.useCase.calibrator

import ir.khebrati.audiosense.domain.model.VolumeRecordPerFrequency

class HeadphoneCalibratorImpl : HeadphoneCalibrator {
    /**
     * Calculates calibration coefficients based on the provided raw data.
     */
    override fun calibrate(rawData: Map<Int, VolumeRecordPerFrequency>): Map<Int, Int> {
        //TODO do calibration
        return emptyMap()
    }
}