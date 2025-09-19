package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.ResultRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.TestRoute
import ir.khebrati.audiosense.presentation.screens.test.NavigationEvent.*
import ir.khebrati.audiosense.presentation.screens.test.components.AnimatedTestVisualizer
import ir.khebrati.audiosense.presentation.theme.AppTheme
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinNavViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TestScreen(
    testRoute: TestRoute,
    onNavigateResult: (ResultRoute) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: TestViewModel = koinNavViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collectLatest {
            when (it) {
                is NavigateToResult -> onNavigateResult(it.route)
            }
        }
    }
    val uiState = viewModel.uiState.collectAsState().value
    AudiosenseScaffold(
        screenTitle = testRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack,
    ) {
        TestScreenContent(
            uiState = uiState,
            onUiAction = {
                if (uiState.progress >= 1f) onNavigateResult(ResultRoute("dummy-test-id"))
                viewModel.onUiAction(it)
            },
        )
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
        modifier =
            Modifier.fillMaxSize().clickable(interactionSource = null, indication = null) {
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
    AppTheme { TestScreenContent(onUiAction = {}, uiState = TestUiState()) }
}
