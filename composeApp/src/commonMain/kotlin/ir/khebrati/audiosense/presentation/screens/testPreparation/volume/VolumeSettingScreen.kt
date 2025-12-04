package ir.khebrati.audiosense.presentation.screens.testPreparation.volume

import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.testPreparation.components.TestSetupLayout

@Composable
fun VolumeSettingScreen(
    volumeRoute: VolumeRoute,
    onNavigateTap: (TapRoute) -> Unit,
    onNavigateBack: () -> Unit,
) {
    TestSetupLayout(
        title = volumeRoute.title,
        onNavigateBack = onNavigateBack,
        illustrationName = "Setting",
        onClickNext = { onNavigateTap(TapRoute) },
        onClickSkip = {},
    ) {}
}
