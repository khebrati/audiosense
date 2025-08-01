package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Results
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Test

@Composable
fun TestScreen(
    testRoute:
    Test,
    onNavigateResult: (
        Results
    ) -> Unit,
) {
    Button(
        onClick = {
            onNavigateResult(
                Results()
            )
        }
    ) {
        Text("Go to Results")
    }
}
