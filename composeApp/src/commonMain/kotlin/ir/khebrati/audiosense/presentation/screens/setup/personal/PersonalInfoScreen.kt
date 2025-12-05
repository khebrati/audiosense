package ir.khebrati.audiosense.presentation.screens.setup.personal

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.screens.setup.components.TestSetupLayout
import ir.khebrati.audiosense.presentation.screens.setup.navigation.SetupInternalRoute.*

@Composable
fun PersonalInfoScreen(
    personalInfoRoute: PersonalInfoRoute,
    pagerState: PagerState,
    onNavigateVolume: (VolumeRoute) -> Unit,
    onNavigateBack: () -> Unit,
    onClickSkip: () -> Unit,
) {
    val ageTextFieldState = rememberTextFieldState()
    val nameTextFieldState = rememberTextFieldState()
    val nextButtonEnabled =
        derivedStateOf {
                val text = ageTextFieldState.text
                text.isNotBlank() && text.all { it.isDigit() }
            }
            .value
    TestSetupLayout(
        title = personalInfoRoute.title,
        onNavigateBack = onNavigateBack,
        illustrationName = "Question",
        onClickNext = {
            onNavigateVolume(VolumeRoute)
        },
        onClickSkip = onClickSkip,
        pagerState = pagerState,
        nextButtonEnabled = nextButtonEnabled,
    ) {
        PersonalInfo(
            ageTextFieldState = ageTextFieldState,
            nameTextFieldState = nameTextFieldState,
        )
    }
}

@Composable
fun PersonalInfo(
    modifier: Modifier = Modifier,
    ageTextFieldState: TextFieldState,
    nameTextFieldState: TextFieldState,
) {
    Column(
        modifier =
            modifier
                .width(300.dp)
                .scrollable(state = rememberScrollState(), orientation = Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text("Tell us about you", style = MaterialTheme.typography.titleLargeEmphasized)
        InfoTextField(
            placeHolder = "Age (Required)",
            state = ageTextFieldState,
        )
        InfoTextField(placeHolder = "Name (Optional)", state = nameTextFieldState)
        HearingAidsSegmentedButtons()
    }
}

@Composable
private fun HearingAidsSegmentedButtons(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Do you use hearing aids?", style = MaterialTheme.typography.labelMediumEmphasized)
            val choices = listOf("No", "Yes")
            var selectedIndex by remember { mutableStateOf(0) }
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth(), space = 25.dp) {
                choices.forEachIndexed { index, label ->
                    SegmentedButton(
                        selected = index == selectedIndex,
                        onClick = { selectedIndex = index },
                        label = { Text(label) },
                        shape = MaterialTheme.shapes.medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoTextField(
    modifier: Modifier = Modifier,
    placeHolder: String,
    state: TextFieldState,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        state = state,
        placeholder = { Text(placeHolder) },
    )
}
