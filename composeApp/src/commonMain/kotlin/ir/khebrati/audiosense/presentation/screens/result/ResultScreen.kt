package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResultRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.HomeRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.ResultsRoute
import ir.khebrati.audiosense.presentation.screens.result.components.Audiogram
import ir.khebrati.audiosense.presentation.screens.result.components.DoneButton
import ir.khebrati.audiosense.presentation.screens.result.components.HearingLossCard
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ResultScreen(
    resultRoute: ResultsRoute,
    onNavigateHome: (HomeRoute) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResultRoute) -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = resultRoute.title,
        canNavigateBack = true,
        onNavigateBack = { onNavigateHome(HomeRoute) },
    ) {
        //        ResultScreenContent(onNavigateBack, onNavigateDescriptiveResult,)
    }
}

@Composable
private fun ResultScreenContent(
    onNavigateHome: (HomeRoute) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResultRoute) -> Unit,
    uiState: TestResultUiState,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val maxCardSize = 175.dp
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().height(maxCardSize),
        ) {
            HearingLossCard(
                lossDbHl = uiState.averageRightHearingLossDBHL,
                side = SideUiState.RIGHT,
                modifier = Modifier.weight(10f).widthIn(max = maxCardSize),
            )
            Spacer(modifier = Modifier.weight(1f))
            HearingLossCard(
                lossDbHl = uiState.averageLeftHearingLossDBHL,
                side = SideUiState.LEFT,
                modifier = Modifier.weight(10f).widthIn(max = maxCardSize),
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
            TestResultUiState(
                averageLeftHearingLossDBHL = 46,
                averageRightHearingLossDBHL = 30,
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
            )
        )
    }
    AppTheme {
        AudiosenseScaffold(screenTitle = "Results", canNavigateBack = true, onNavigateBack = {}) {
            ResultScreenContent({}, {}, uiState)
        }
    }
}
