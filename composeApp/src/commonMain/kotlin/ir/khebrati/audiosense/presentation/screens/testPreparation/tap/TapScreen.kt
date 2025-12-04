package ir.khebrati.audiosense.presentation.screens.testPreparation.tap

import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.testPreparation.components.TestSetupLayout

@Composable
fun TapScreen(
    tapRoute: TapRoute,
    onNavigateSelectDevice: (SelectDeviceRoute) -> Unit,
    onNavigateBack: () -> Unit,
) {
    TestSetupLayout(
        title = tapRoute.title,
        onNavigateBack = onNavigateBack,
        illustrationName = "Tap",
        onClickNext = { onNavigateSelectDevice(SelectDeviceRoute) },
        onClickSkip = {},
    ) {}
}
