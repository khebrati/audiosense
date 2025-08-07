package ir.khebrati.audiosense.domain.model

data class VolumeRecordPerFrequency(
    val playedVolumeDbSpl: Int,
    val measuredVolumeDbSpl: Int,
)