package ir.khebrati.audiosense.domain.model

enum class DefaultHeadphones(val model: String) {
    GalaxyBudsFE("Galaxy Buds FE"),
    AppleAirpods("Apple Airpods"),
    SonyHeadphones("Sony Headphones"),
    Uncalibrated("Default"),
}

fun isDefaultHeadphone(model: String) = (model in DefaultHeadphones.entries.map { it.model })
