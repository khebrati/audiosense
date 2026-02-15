package ir.khebrati.audiosense.data

import ir.khebrati.audiosense.data.source.local.entity.LocalHeadphone
import ir.khebrati.audiosense.data.source.local.entity.LocalTest
import ir.khebrati.audiosense.data.source.remote.entity.RemoteHeadphone
import ir.khebrati.audiosense.domain.model.Headphone
import ir.khebrati.audiosense.domain.model.VolumeRecordPerFrequency
import ir.khebrati.audiosense.domain.model.Test

fun LocalTest.toExternal(usedHeadphone: Headphone) =
    Test(
        id = id,
        dateTime = dateTime,
        noiseDuringTest = noiseDuringTest,
        leftAC = leftAC,
        rightAC = rightAC,
        headphone = usedHeadphone,
        personName = personName,
        personAge = personAge,
        hasHearingAidExperience = hasHearingAidExperience,
    )

fun Map<LocalHeadphone, List<LocalTest>>.toExternal(): List<Test> {
    return this.flatMap { (headphone, tests) ->
        tests.map { test -> test.toExternal(headphone.toExternal()) }
    }
}

fun Test.toLocal() =
    LocalTest(
        id = id,
        headphoneId = headphone.id,
        dateTime = dateTime,
        personName = personName,
        personAge = personAge,
        hasHearingAidExperience = hasHearingAidExperience,
        noiseDuringTest = noiseDuringTest,
        leftAC = leftAC,
        rightAC = rightAC,
    )

fun List<LocalHeadphone>.toExternal(): List<Headphone> {
    return this.map { it.toExternal() }
}

fun LocalHeadphone.toExternal() =
    Headphone(
        id = id,
        model = model,
        calibrationCoefficients = calibrationCoefficients,
    )

private fun Map<Int, Pair<Int, Int>>.toExternal() =
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

