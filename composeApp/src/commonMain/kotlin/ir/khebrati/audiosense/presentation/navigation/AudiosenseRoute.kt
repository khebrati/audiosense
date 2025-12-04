package ir.khebrati.audiosense.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class AudiosenseRoute(val title: String){
    @Serializable
     object HomeRoute : AudiosenseRoute("Home")

    @Serializable
     object SelectDeviceRoute : AudiosenseRoute("New Test")
    @Serializable
     object PersonalInfoRoute : AudiosenseRoute("About You")

    @Serializable
    object VolumeRoute : AudiosenseRoute("Adjust Setting")

    @Serializable
    object TapRoute : AudiosenseRoute("Tap")

    @Serializable
    data class TestRoute(val selectedHeadphoneId: String) : AudiosenseRoute("Hearing Test")

    @Serializable
    data class ResultRoute(val testId: String) : AudiosenseRoute("Test Results")

    @Serializable
     object DescriptiveResultRoute : AudiosenseRoute("Your Hearing World")

    @Serializable
     object SettingRoute : AudiosenseRoute("Settings")

    @Serializable
     object CalibrationRoute : AudiosenseRoute("Calibration")
}

