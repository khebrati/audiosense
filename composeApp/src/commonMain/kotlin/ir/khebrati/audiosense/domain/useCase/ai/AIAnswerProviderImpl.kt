package ir.khebrati.audiosense.domain.useCase.ai

class AIAnswerProviderImpl : AIAnswerProvider {
    override fun provide(
        leftAudiogram: Map<Int, Int>,
        rightAudiogram: Map<Int, Int>,
        age: Int,
        leftWhoCategory: String,
        rightWhoCategory: String,
        usesHearingAid: Boolean
    ): String {
        TODO("Not yet implemented")
    }
}