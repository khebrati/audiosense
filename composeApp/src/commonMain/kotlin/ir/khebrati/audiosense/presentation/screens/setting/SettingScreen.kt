package ir.khebrati.audiosense.presentation.screens.setting

import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Setting

@Composable
fun SettingScreen(
    settingRoute : Setting,
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
