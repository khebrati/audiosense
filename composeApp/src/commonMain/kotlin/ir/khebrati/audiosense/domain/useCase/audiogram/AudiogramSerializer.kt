package ir.khebrati.audiosense.domain.useCase.audiogram

interface AudiogramSerializer {
    fun serialize(
        leftAC: Map<Int,Int>,
        rightAC: Map<Int,Int>
    ) : String
}
