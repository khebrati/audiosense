package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.khebrati.audiosense.presentation.components.AudiosenseAppBar
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.components.LoadingScreen
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResultRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.HomeRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.ResultRoute
import ir.khebrati.audiosense.presentation.screens.result.TestResultIntent.*
import ir.khebrati.audiosense.presentation.screens.result.TestResultUiState.*
import ir.khebrati.audiosense.presentation.screens.result.components.Audiogram
import ir.khebrati.audiosense.presentation.screens.result.components.DoneButton
import ir.khebrati.audiosense.presentation.screens.result.components.HearingLossCard
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinNavViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    resultRoute: ResultRoute,
    onNavigateHome: (HomeRoute) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResultRoute) -> Unit,
    viewModel: TestResultViewModel = koinNavViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    AudiosenseScaffold(
        topBar = {
            AudiosenseAppBar(
                title = resultRoute.title,
                canNavigateBack = true,
                actions = {
                    IconButton(onClick = {
                        viewModel.handleIntent(Share)
                    }, modifier = Modifier.size(50.dp)){
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share test results")
                    }
                },
                onNavigateBack = { onNavigateHome(HomeRoute) },
            )
        }
    ) {
        if (uiState is Ready) {
            ReadyResultScreenContent(onNavigateHome, onNavigateDescriptiveResult, uiState)
        } else LoadingScreen()
    }
}

@Composable
private fun ReadyResultScreenContent(
    onNavigateHome: (HomeRoute) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResultRoute) -> Unit,
    uiState: Ready,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val maxCardSize = 175.dp
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().height(maxCardSize),
        ) {
            HearingLossCard(
                lossDbHl = uiState.generalRightHearingLoss,
                side = SideUiState.RIGHT,
                modifier = Modifier.weight(10f).widthIn(max = maxCardSize),
                describedLossLevel = uiState.describedLeftHearingLoss,
            )
            Spacer(modifier = Modifier.weight(1f))
            HearingLossCard(
                lossDbHl = uiState.generalLeftHearingLoss,
                side = SideUiState.LEFT,
                modifier = Modifier.weight(10f).widthIn(max = maxCardSize),
                describedLossLevel = uiState.describedRightHearingLoss,
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Audiogram(
            leftAC = uiState.leftAC,
            rightAC = uiState.rightAC,
            modifier = Modifier.fillMaxWidth().height(300.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        DoneButton(onClick = { onNavigateHome(HomeRoute) })
    }
}

@Preview
@Composable
fun PreviewResultScreen() {
    val uiState by remember {
        mutableStateOf(
            Ready(
                generalLeftHearingLoss = 46,
                generalRightHearingLoss = 30,
                leftAC =
                    hashMapOf(
                        125 to 90,
                        250 to 50,
                        500 to 20,
                        1000 to 20,
                        2000 to 30,
                        4000 to 55,
                        8000 to 35,
                    ),
                rightAC =
                    hashMapOf(
                        125 to 30,
                        250 to 30,
                        500 to 5,
                        1000 to 0,
                        2000 to 0,
                        4000 to 5,
                        8000 to 5,
                    ),
                describedRightHearingLoss = "Normal",
                describedLeftHearingLoss = "Profound",
            )
        )
    }
    AppTheme {
        AudiosenseScaffold(
            topBar = {
                AudiosenseAppBar(title = "Results", canNavigateBack = true, onNavigateBack = {})
            }
        ) {
            ReadyResultScreenContent({}, {}, uiState)
        }
    }
}
