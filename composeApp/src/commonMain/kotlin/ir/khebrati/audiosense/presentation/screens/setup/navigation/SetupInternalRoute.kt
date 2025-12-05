package ir.khebrati.audiosense.presentation.screens.setup.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SetupInternalRoute(val title : String = "Get Ready") {
    @Serializable
    object PersonalInfoRoute : SetupInternalRoute()

    @Serializable
    object VolumeRoute : SetupInternalRoute()

    @Serializable
    object TapRoute : SetupInternalRoute()

    @Serializable
    object SelectDeviceRoute : SetupInternalRoute()
}
