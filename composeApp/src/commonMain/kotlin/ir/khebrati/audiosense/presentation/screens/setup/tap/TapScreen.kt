package ir.khebrati.audiosense.presentation.screens.setup.tap

import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.setup.components.IllustrationLoader
import ir.khebrati.audiosense.presentation.screens.setup.components.TestSetupLayout
import ir.khebrati.audiosense.presentation.screens.setup.components.TipCard
import ir.khebrati.audiosense.presentation.screens.setup.navigation.SetupInternalRoute.*

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
        //        illustrationName = "Tap",
        pagerState = pagerState,
        onClickNext = { onNavigateSelectDevice(SelectDeviceRoute) },
        onClickSkip = {},
    ) {
        IllustrationLoader(modifier = Modifier.width(300.dp), illustrationName = "Tap") {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.scrollable(rememberScrollState(), orientation = Vertical),
            ) {
                Spacer(modifier = Modifier.height(14.dp))
                TipCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.TouchApp,
                    iconDescription = "Touch on app",
                    title = "Touch for feedback",
                    body =
                        "You will hear faint tones. Tap the screen anywhere as soon as you hear a sound.",
                )
            }
        }
    }
}
