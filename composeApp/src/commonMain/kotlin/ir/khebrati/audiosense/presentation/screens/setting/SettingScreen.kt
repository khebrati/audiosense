package ir.khebrati.audiosense.presentation.screens.setting

import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SettingRoute

@Composable
fun SettingScreen(
    settingRoute : SettingRoute,
    onNavigateBack: () -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = settingRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack
,
    ){

    }
}
