package ir.khebrati.audiosense.data

import ir.khebrati.audiosense.data.source.local.entity.LocalHeadphone
import ir.khebrati.audiosense.data.source.local.entity.LocalTest
import ir.khebrati.audiosense.domain.model.Headphone
import ir.khebrati.audiosense.domain.model.Test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun LocalTest.toExternal(usedHeadphone: Headphone) = Test(
    id = id,
    dateTime = dateTime,
    noiseDuringTest = noiseDuringTest,
    leftAC = leftAC,
    rightAC = rightAC,
    headphone = usedHeadphone
)

fun Map<LocalHeadphone,List<LocalTest>>.toExternal() : List<Test> {
    return this.flatMap { (headphone, tests) ->
        tests.map { test ->
            test.toExternal(headphone.toExternal())
        }
    }
}

fun Test.toLocal() = LocalTest(
    id = id,
    headphoneId = headphone.id,
    dateTime = dateTime,
    noiseDuringTest = noiseDuringTest,
    leftAC = leftAC,
    rightAC = rightAC
)
fun List<LocalHeadphone>.toExternal() : List<Headphone> {
    return this.map { it.toExternal() }
}
fun LocalHeadphone.toExternal() = Headphone(
    id = id,
    model = model,
    calibrationCoefficients = calibrationCoefficients
)

fun Headphone.toLocal() = LocalHeadphone(
    id = id,
    model = model,
    calibrationCoefficients = calibrationCoefficients
)