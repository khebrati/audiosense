package ir.khebrati.audiosense.domain.model

enum class DefaultHeadphonesName(val value: String) {
    GalaxyBudsFE("Galaxy Buds FE"),
    AppleAirpods("Apple Airpods"),
    SonyHeadphones("Sony Headphones"),
    Uncalibrated("Default"),
}

fun isDefaultHeadphone(model: String) = (model in DefaultHeadphonesName.entries.map { it.value })
