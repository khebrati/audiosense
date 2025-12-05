package ir.khebrati.audiosense.presentation.screens.setup.volume

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.setup.components.TestSetupLayout
import ir.khebrati.audiosense.presentation.screens.setup.navigation.SetupInternalRoute
import ir.khebrati.audiosense.presentation.screens.setup.navigation.SetupInternalRoute.*

@Composable
fun VolumeSettingScreen(
    volumeRoute: VolumeRoute,
    pagerState: PagerState,
    onNavigateTap: (TapRoute) -> Unit,
    onNavigateBack: () -> Unit,
) {
    TestSetupLayout(
        title = volumeRoute.title,
        onNavigateBack = onNavigateBack,
        illustrationName = "Setting",
        pagerState = pagerState,
        onClickNext = { onNavigateTap(TapRoute) },
        onClickSkip = {},
    ) {}
}
