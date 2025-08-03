package ir.khebrati.audiosense.presentation.navigation

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass


@Serializable
sealed class AudiosenseRoute(val title: String){
    @Serializable
     object Home : AudiosenseRoute("Home")

    @Serializable
     object SelectDevice : AudiosenseRoute("New Test")
    @Serializable
     object NoiseMeter : AudiosenseRoute("New Test")

    @Serializable
     object Test : AudiosenseRoute("Hearing Test")

    @Serializable
     object Results : AudiosenseRoute("Test Results")

    @Serializable
     object DescriptiveResult : AudiosenseRoute("Your Hearing World")

    @Serializable
     object Setting : AudiosenseRoute("Settings")

    @Serializable
     object Calibration : AudiosenseRoute("Calibration")
}

