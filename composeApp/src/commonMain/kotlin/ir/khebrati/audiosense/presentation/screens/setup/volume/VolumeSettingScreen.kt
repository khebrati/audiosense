package ir.khebrati.audiosense.presentation.screens.setup.volume

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
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.setup.components.IllustrationLoader
import ir.khebrati.audiosense.presentation.screens.setup.components.TestSetupLayout
import ir.khebrati.audiosense.presentation.screens.setup.components.TipCard
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
        pagerState = pagerState,
        onClickNext = {
            onNavigateTap(TapRoute(
                personName = volumeRoute.personName,
                personAge = volumeRoute.personAge,
                hasHearingAidExperience = volumeRoute.hasHearingAidExperience
            ))
        },
        onClickSkip = {},
    ) {
        IllustrationLoader(modifier = Modifier.width(300.dp), illustrationName = "Settings") {
            Spacer(modifier = Modifier.height(14.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.scrollable(rememberScrollState(), orientation = Vertical)
            ) {
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
                    body =
                        "Background noise will effect your score. Please move to a very quiet room.",
                )
            }
        }
    }
}
