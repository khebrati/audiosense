package ir.khebrati.audiosense.domain.useCase.ai

interface AIAnswerProvider {
    fun provide(
        leftAudiogram: Map<Int,Int>,
        rightAudiogram: Map<Int,Int>,
        age: Int,
        leftWhoCategory: String,
        rightWhoCategory: String,
        usesHearingAid: Boolean
    ) : String
}
