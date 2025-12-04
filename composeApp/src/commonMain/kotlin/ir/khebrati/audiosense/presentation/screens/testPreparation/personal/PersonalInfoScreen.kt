package ir.khebrati.audiosense.presentation.screens.testPreparation.personal

import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.PersonalInfoRoute
import ir.khebrati.audiosense.presentation.screens.testPreparation.components.TestSetupLayout

@Composable
fun PersonalInfoScreen(
    personalInfoRoute: PersonalInfoRoute,
    onNavigateVolume: (AudiosenseRoute.VolumeRoute) -> Unit,
    onNavigateBack: () -> Unit,
) {
    TestSetupLayout(
        title = personalInfoRoute.title,
        onNavigateBack = onNavigateBack,
        illustrationName = "Question",
        onClickNext = { onNavigateVolume(AudiosenseRoute.VolumeRoute) },
        onClickSkip = {},

    ) {}
}
