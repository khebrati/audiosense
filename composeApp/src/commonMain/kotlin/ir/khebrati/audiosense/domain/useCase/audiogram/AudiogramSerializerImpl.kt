package ir.khebrati.audiosense.domain.useCase.audiogram

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonTransformingSerializer

class AudiogramSerializerImpl : AudiogramSerializer {
    override fun serialize(
        leftAC: Map<Int, Int>,
        rightAC: Map<Int, Int>
    ) : String{
        val audiogram = Audiogram(
            leftAC = leftAC,
            rightAC = rightAC
        )
        return Json.encodeToString(audiogram)
    }
}

@Serializable
private data class Audiogram(
    val leftAC: Map<Int,Int>,
    val rightAC: Map<Int,Int>
)