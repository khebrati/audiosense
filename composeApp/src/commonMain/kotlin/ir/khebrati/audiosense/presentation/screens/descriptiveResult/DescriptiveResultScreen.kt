package ir.khebrati.audiosense.presentation.screens.descriptiveResult

import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResultRoute

@Composable
fun DescriptiveResultScreen(
    descriptiveResultRoute: DescriptiveResultRoute,
    onNavigateBack: () -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = descriptiveResultRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack,
    ){

    }
}
