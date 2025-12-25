package ir.khebrati.audiosense.presentation.screens.setup.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SetupInternalRoute(val title : String = "Get Ready") {
    @Serializable
    object PersonalInfoRoute : SetupInternalRoute()

    @Serializable
    data class VolumeRoute(
        val personName: String?,
        val personAge: Int,
        val hasHearingAidExperience: Boolean
    ) : SetupInternalRoute()

    @Serializable
    data class TapRoute(
        val personName: String?,
        val personAge: Int,
        val hasHearingAidExperience: Boolean
    ) : SetupInternalRoute()

    @Serializable
    data class SelectDeviceRoute(
        val personName: String?,
        val personAge: Int,
        val hasHearingAidExperience: Boolean
    ) : SetupInternalRoute()
}
