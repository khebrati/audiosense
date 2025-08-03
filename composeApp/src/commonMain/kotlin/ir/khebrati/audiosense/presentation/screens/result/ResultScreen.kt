package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResult
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Home
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Results

@Composable
fun ResultScreen(
    resultRoute: Results,
    onNavigateBack: (Home) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResult) -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = resultRoute.title,
        canNavigateBack = true,
        onNavigateBack = {
            onNavigateBack(Home)
        },
    ){
        Column {
            Button(
                onClick = { onNavigateBack(Home) }
            ){
                Text("Back to Home")
            }
            Button(
                onClick = { onNavigateDescriptiveResult(DescriptiveResult) }
            ){
                Text("View Descriptive Result")
            }
        }
    }
}
