package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Results
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Test
import ir.khebrati.audiosense.presentation.screens.test.components.AnimatedTestVisualizer
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinNavViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TestScreen(
    testRoute: Test,
    onNavigateResult: (Results) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: TestViewModel = koinNavViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    AudiosenseScaffold(
        screenTitle = testRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack,
    ) {
        TestScreenContent(uiState = uiState, onUiAction = viewModel::onUiAction)
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun TestScreenContent(uiState: TestUiState, onUiAction: (TestUiAction) -> Unit) {
    val animatedProgress by
        animateFloatAsState(
            targetValue = uiState.progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )
    LinearWavyProgressIndicator(progress = { animatedProgress }, modifier = Modifier.fillMaxWidth())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize().clickable(
            interactionSource = null,
            indication = null
        ) {
            onUiAction(TestUiAction.OnClick)
        },
    ) {
        Text("Left Ear", style = MaterialTheme.typography.titleMedium)
        AnimatedTestVisualizer(modifier = Modifier.fillMaxWidth().height(300.dp))
        Text(
            "Tab anywhere when you hear a tone",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TestScreenPreview() {
    AppTheme {
        TestScreenContent(
            onUiAction = {},
            uiState = TestUiState()
        )
    }
}
