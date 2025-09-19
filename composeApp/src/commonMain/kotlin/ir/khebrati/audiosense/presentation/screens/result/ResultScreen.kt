package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResultRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.HomeRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.ResultsRoute

@Composable
fun ResultScreen(
    resultRoute: ResultsRoute,
    onNavigateBack: (HomeRoute) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResultRoute) -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = resultRoute.title,
        canNavigateBack = true,
        onNavigateBack = {
            onNavigateBack(HomeRoute)
        },
    ){
        ResultScreenContent(onNavigateBack, onNavigateDescriptiveResult)
    }
}

@Composable
private fun ResultScreenContent(
    onNavigateBack: (HomeRoute) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResultRoute) -> Unit
) {
    Column {
        Button(
            onClick = { onNavigateBack(HomeRoute) }
        ) {
            Text("Back to Home")
        }
        Button(
            onClick = { onNavigateDescriptiveResult(DescriptiveResultRoute) }
        ) {
            Text("View Descriptive Result")
        }
    }
}
