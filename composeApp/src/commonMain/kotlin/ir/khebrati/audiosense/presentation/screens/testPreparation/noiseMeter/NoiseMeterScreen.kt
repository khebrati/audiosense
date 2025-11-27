package ir.khebrati.audiosense.presentation.screens.testPreparation.noiseMeter

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.components.AudiosenseAppBar
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.NoiseMeterRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.TestRoute

@Composable
fun NoiseMeterScreen(
    noiseMeterRoute : NoiseMeterRoute,
    onNavigateTest: (TestRoute) -> Unit,
    onNavigateBack: () -> Unit,
) {
    AudiosenseScaffold(
        topBar = {
            AudiosenseAppBar(
                title = noiseMeterRoute.title,
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        },
    ) {
        Button(
            onClick = {
//                onNavigateTest(TestRoute)
            }
        ) {
            Text("Get noise")
        }
    }
}