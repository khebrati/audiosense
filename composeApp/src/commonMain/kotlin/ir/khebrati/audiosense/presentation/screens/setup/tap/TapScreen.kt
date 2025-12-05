package ir.khebrati.audiosense.presentation.screens.setup.tap

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.setup.components.TestSetupLayout
import ir.khebrati.audiosense.presentation.screens.setup.navigation.SetupInternalRoute
import ir.khebrati.audiosense.presentation.screens.setup.navigation.SetupInternalRoute.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun TapScreen(
    tapRoute: TapRoute,
    pagerState: PagerState,
    onNavigateSelectDevice: (SelectDeviceRoute) -> Unit,
    onNavigateBack: () -> Unit,
) {
    TestSetupLayout(
        title = tapRoute.title,
        onNavigateBack = onNavigateBack,
        illustrationName = "Tap",
        pagerState = pagerState,
        onClickNext = { onNavigateSelectDevice(SelectDeviceRoute) },
        onClickSkip = {},
    ) {}
}
