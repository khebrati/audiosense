package ir.khebrati.audiosense.presentation.screens.testPreparation.noiseMeter

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.components.AudiosenseAppBar
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.PersonalInfoRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.TestRoute
import ir.khebrati.audiosense.presentation.screens.testPreparation.components.TestSetupBottomBar

@Composable
fun PersonalInfoScreen(
    noiseMeterRoute : PersonalInfoRoute,
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
        bottomBar = {
            TestSetupBottomBar(
                modifier = Modifier.height(100.dp).fillMaxWidth(),
                onClick = {},
                count = 3
            )
        }
    ){}
}