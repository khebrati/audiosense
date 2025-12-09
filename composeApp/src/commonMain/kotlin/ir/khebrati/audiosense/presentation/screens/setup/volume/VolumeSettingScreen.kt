package ir.khebrati.audiosense.presentation.screens.setup.volume

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.setup.components.TestSetupLayout
import ir.khebrati.audiosense.presentation.screens.setup.components.TipCard
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
    ) {
        Row {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(modifier = Modifier.height(14.dp))
                TipCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.AutoMirrored.Outlined.VolumeUp,
                    iconDescription = "Volume up",
                    title = "Maximize Volume",
                    body = "Set your phone to 100% volume for the test to have accurate results.",
                )
                TipCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.GraphicEq,
                    iconDescription = "Find silence",
                    title = "Find Silence",
                    body = "Background noise will effect your score. Please move to a very quiet room.",
                )
            }
        }
    }
}
