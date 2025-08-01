package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResult
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Home
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Results

@Composable
fun ResultScreen(
    resultRoute: Results,
    onNavigateHome: (Home) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResult) -> Unit,
) {
    Column {
        Button(
            onClick = { onNavigateHome(Home()) }
        ){
            Text("Back to Home")
        }
        Button(
            onClick = { onNavigateDescriptiveResult(DescriptiveResult()) }
        ){
            Text("View Descriptive Result")
        }
    }
}
