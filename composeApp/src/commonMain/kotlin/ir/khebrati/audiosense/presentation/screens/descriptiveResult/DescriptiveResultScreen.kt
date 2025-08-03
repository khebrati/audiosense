package ir.khebrati.audiosense.presentation.screens.descriptiveResult

import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResult

@Composable
fun DescriptiveResultScreen(
    descriptiveResultRoute: DescriptiveResult,
    onNavigateBack: () -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = descriptiveResultRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack,
    ){

    }
}
