package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
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
    ClickableRippleEffect(
        onClick = {viewModel.onUiAction(TestUiAction.OnClick)}
    ) {
        AudiosenseScaffold(
            screenTitle = testRoute.title,
            canNavigateBack = true,
            onNavigateBack = onNavigateBack,
        ) {
            TestScreenContent(
                uiState = uiState,
            )
        }
    }
}

@Composable
private fun ClickableRippleEffect(modifier: Modifier = Modifier, onClick: () -> Unit, content: @Composable () -> Unit) {
    val localHaptic = LocalHapticFeedback.current
    Box(
        modifier = modifier.clickable(
            onClick = {
                onClick()
                localHaptic.performHapticFeedback(HapticFeedbackType.Confirm)
            },
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple()
        )
    ) {
        content()
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun TestScreenContent(uiState: TestUiState) {
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
            Modifier.fillMaxSize()
    ) {
        Text("${uiState.side} Ear", style = MaterialTheme.typography.titleMedium)
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
//    AppTheme { TestScreenContent(onUiAction = {}, uiState = TestUiState()) }
}
