package ir.khebrati.audiosense.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class AudiosenseRoute{
    @Serializable
     class Home : AudiosenseRoute()

    @Serializable
     class SelectDevice : AudiosenseRoute()
    @Serializable
     class NoiseMeter : AudiosenseRoute()

    @Serializable
     class Test : AudiosenseRoute()

    @Serializable
     class Results : AudiosenseRoute()

    @Serializable
     class DescriptiveResult : AudiosenseRoute()

    @Serializable
     class Setting : AudiosenseRoute()

    @Serializable
     class Calibration : AudiosenseRoute()
}

