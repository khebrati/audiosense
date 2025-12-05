package ir.khebrati.audiosense.presentation.screens.setup.personal

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.screens.setup.components.TestSetupLayout
import ir.khebrati.audiosense.presentation.screens.setup.navigation.SetupInternalRoute.*

@Composable
fun PersonalInfoScreen(
    personalInfoRoute: PersonalInfoRoute,
    pagerState: PagerState,
    onNavigateVolume: (VolumeRoute) -> Unit,
    onNavigateBack: () -> Unit,
    onClickSkip: () -> Unit,
) {
    TestSetupLayout(
        title = personalInfoRoute.title,
        onNavigateBack = onNavigateBack,
        illustrationName = "Question",
        onClickNext = { onNavigateVolume(VolumeRoute) },
        onClickSkip = onClickSkip,
        pagerState = pagerState
    ) {}
}
