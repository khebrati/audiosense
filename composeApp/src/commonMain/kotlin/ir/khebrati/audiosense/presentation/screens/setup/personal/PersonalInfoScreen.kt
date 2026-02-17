package ir.khebrati.audiosense.presentation.screens.setup.personal

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.Orientation.*
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
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.screens.setup.components.IllustrationLoader
import ir.khebrati.audiosense.presentation.screens.setup.components.TestSetupLayout
import ir.khebrati.audiosense.presentation.screens.setup.navigation.SetupInternalRoute.*
import org.koin.compose.koinInject

@Composable
fun PersonalInfoScreen(
    personalInfoRoute: PersonalInfoRoute,
    pagerState: PagerState,
    onNavigateVolume: (VolumeRoute) -> Unit,
    onNavigateBack: () -> Unit,
    onClickSkip: () -> Unit,
    viewModel: PersonalInfoViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val ageTextFieldState = rememberTextFieldState()
    val nameTextFieldState = rememberTextFieldState()

    // Set initial values from the last test when loading completes
    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            if (uiState.initialAge.isNotEmpty()) {
                ageTextFieldState.setTextAndPlaceCursorAtEnd(uiState.initialAge)
            }
            if (uiState.initialName.isNotEmpty()) {
                nameTextFieldState.setTextAndPlaceCursorAtEnd(uiState.initialName)
            }
        }
    }

    var hasHearingAidExperience by remember { mutableStateOf(false) }
    val nextButtonEnabled =
        derivedStateOf {
                val text = ageTextFieldState.text
                text.isNotBlank() && text.all { it.isDigit() }
            }
            .value
    TestSetupLayout(
        title = personalInfoRoute.title,
        onNavigateBack = onNavigateBack,
        onClickNext = {
            val age = ageTextFieldState.text.toString().toIntOrNull() ?: 0
            val name = nameTextFieldState.text.toString().takeIf { it.isNotBlank() }
            onNavigateVolume(VolumeRoute(
                personName = name,
                personAge = age,
                hasHearingAidExperience = hasHearingAidExperience
            ))
        },
        onClickSkip = onClickSkip,
        pagerState = pagerState,
        nextButtonEnabled = nextButtonEnabled,
    ) {
        IllustrationLoader(modifier = Modifier.width(300.dp), illustrationName = "Question") {
            Column(
                modifier = Modifier.fillMaxWidth().scrollable(rememberScrollState(), orientation = Vertical),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PersonalInfo(
                    ageTextFieldState = ageTextFieldState,
                    nameTextFieldState = nameTextFieldState,
                    hasHearingAidExperience = hasHearingAidExperience,
                    onHearingAidExperienceChanged = { hasHearingAidExperience = it }
                )
            }
        }
    }
}

@Composable
fun PersonalInfo(
    modifier: Modifier = Modifier,
    ageTextFieldState: TextFieldState,
    nameTextFieldState: TextFieldState,
    hasHearingAidExperience: Boolean,
    onHearingAidExperienceChanged: (Boolean) -> Unit,
) {
    Column(
        modifier =
            modifier
                .width(300.dp)
                .scrollable(state = rememberScrollState(), orientation = Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text("Tell us about you", style = MaterialTheme.typography.titleLargeEmphasized)
        InfoTextField(placeHolder = "Age (Required)", state = ageTextFieldState)
        InfoTextField(placeHolder = "Name (Optional)", state = nameTextFieldState)
        HearingAidsSegmentedButtons(
            hasHearingAidExperience = hasHearingAidExperience,
            onHearingAidExperienceChanged = onHearingAidExperienceChanged
        )
    }
}

@Composable
private fun HearingAidsSegmentedButtons(
    modifier: Modifier = Modifier,
    hasHearingAidExperience: Boolean,
    onHearingAidExperienceChanged: (Boolean) -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Do you use hearing aids?", style = MaterialTheme.typography.labelMediumEmphasized)
            Spacer(modifier = Modifier.height(5.dp))
            val choices = remember { listOf("No", "Yes") }
            val selectedIndex = if (hasHearingAidExperience) 1 else 0
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth(), space = 25.dp) {
                choices.forEachIndexed { index, label ->
                    SegmentedButton(
                        modifier = Modifier.height(50.dp),
                        selected = index == selectedIndex,
                        onClick = { onHearingAidExperienceChanged(index == 1) },
                        label = { Text(label) },
                        shape = MaterialTheme.shapes.medium,
                        colors =
                            SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
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
