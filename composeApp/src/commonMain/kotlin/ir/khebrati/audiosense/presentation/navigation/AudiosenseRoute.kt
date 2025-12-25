package ir.khebrati.audiosense.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class AudiosenseRoute(val title: String){
    @Serializable
     object HomeRoute : AudiosenseRoute("Home")

    @Serializable
    data object TestSetupRoute: AudiosenseRoute("Get Ready")
    @Serializable
    data class TestRoute(
        val selectedHeadphoneId: String,
        val personName: String?,
        val personAge: Int,
        val hasHearingAidExperience: Boolean
    ) : AudiosenseRoute("Hearing Test")

    @Serializable
    data class ResultRoute(val testId: String) : AudiosenseRoute("Test Results")

    @Serializable
     object DescriptiveResultRoute : AudiosenseRoute("Your Hearing World")

    @Serializable
     object SettingRoute : AudiosenseRoute("Settings")

    @Serializable
     object CalibrationRoute : AudiosenseRoute("Calibration")
}

