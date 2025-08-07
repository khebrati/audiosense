package ir.khebrati.audiosense.data

import ir.khebrati.audiosense.data.source.local.entity.LocalHeadphone
import ir.khebrati.audiosense.data.source.local.entity.LocalTest
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
        calibrationCoefficients = calibrationCoefficients.toExternal(),
    )

private fun Map<Int, Pair<Int, Int>>.toExternal() =
    this.mapValues { VolumeRecordPerFrequency(it.value.first, it.value.second) }

fun Headphone.toLocal() =
    LocalHeadphone(
        id = id,
        model = model,
        calibrationCoefficients = calibrationCoefficients.toLocal(),
    )

fun Map<Int, VolumeRecordPerFrequency>.toLocal(): Map<Int, Pair<Int, Int>> {
    return this.mapValues { Pair(it.value.playedVolumeDbSpl, it.value.measuredVolumeDbSpl) }
}
