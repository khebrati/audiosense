package ir.khebrati.audiosense.data

import ir.khebrati.audiosense.data.source.remote.entity.RemoteHeadphone
import ir.khebrati.audiosense.db.GetAllTestsWithHeadphones
import ir.khebrati.audiosense.db.GetTestWithHeadphone
import ir.khebrati.audiosense.db.LocalHeadphone
import ir.khebrati.audiosense.domain.model.Headphone
import ir.khebrati.audiosense.domain.model.VolumeRecordPerFrequency
import ir.khebrati.audiosense.domain.model.Test

// SqlDelight generated types to domain models
fun GetTestWithHeadphone.toExternalTestList() =
    Test(
        id = id,
        dateTime = dateTime,
        noiseDuringTest = noiseDuringTest.toInt(),
        leftAC = leftAC,
        rightAC = rightAC,
        headphone = Headphone(
            id = headphone_id,
            model = headphone_model,
            calibrationCoefficients = headphone_calibrationCoefficients
        ),
        personName = personName,
        personAge = personAge.toInt(),
        hasHearingAidExperience = hasHearingAidExperience,
    )

fun GetAllTestsWithHeadphones.toExternalTestList() =
    Test(
        id = id,
        dateTime = dateTime,
        noiseDuringTest = noiseDuringTest.toInt(),
        leftAC = leftAC,
        rightAC = rightAC,
        headphone = Headphone(
            id = headphone_id,
            model = headphone_model,
            calibrationCoefficients = headphone_calibrationCoefficients
        ),
        personName = personName,
        personAge = personAge.toInt(),
        hasHearingAidExperience = hasHearingAidExperience,
    )

fun List<GetAllTestsWithHeadphones>.toExternalTestList(): List<Test> {
    return this.map { it.toExternalTestList() }
}

fun List<LocalHeadphone>.toExternal(): List<Headphone> {
    return this.map { it.toExternalTestList() }
}

fun LocalHeadphone.toExternalTestList() =
    Headphone(
        id = id,
        model = model,
        calibrationCoefficients = calibrationCoefficients,
    )

private fun Map<Int, Pair<Int, Int>>.toExternalTestList() =
    this.mapValues { VolumeRecordPerFrequency(it.value.first, it.value.second) }

fun Headphone.toLocal() =
    LocalHeadphone(
        id = id,
        model = model,
        calibrationCoefficients = calibrationCoefficients,
    )

fun Map<Int, VolumeRecordPerFrequency>.toLocal(): Map<Int, Int> {
    return this.mapValues { it.value.measuredVolumeDbSpl -  it.value.playedVolumeDbSpl }
}

// Remote to Local mappings
fun RemoteHeadphone.toLocal() = LocalHeadphone(
    id = _id,
    model = name,
    calibrationCoefficients = calibrationCoefficients.mapValues { it.value.second - it.value.first },
)

fun List<RemoteHeadphone>.toLocal(): List<LocalHeadphone> = map { it.toLocal() }

