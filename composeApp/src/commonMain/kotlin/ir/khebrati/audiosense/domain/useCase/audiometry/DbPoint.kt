package ir.khebrati.audiosense.domain.useCase.audiometry

import ir.khebrati.audiosense.domain.model.SoundPoint
import ir.khebrati.audiosense.domain.useCase.spl.dbSpl

data class DbPoint(
    val db: Int,
    val frequency: Int
)
fun DbPoint.toSoundPoint() =
    SoundPoint(frequency, db.dbSpl)