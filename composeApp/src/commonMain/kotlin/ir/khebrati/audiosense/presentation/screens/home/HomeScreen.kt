package ir.khebrati.audiosense.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import audiosense.composeapp.generated.resources.Res
import audiosense.composeapp.generated.resources.record_not_found
import ir.khebrati.audiosense.domain.model.DefaultHeadphones
import ir.khebrati.audiosense.domain.model.DefaultHeadphones.*
import ir.khebrati.audiosense.domain.useCase.time.TimeOfDay
import ir.khebrati.audiosense.domain.useCase.time.capitalizedName
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.components.HeadphoneIcon
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.CalibrationRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.ResultRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDeviceRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SettingRoute
import ir.khebrati.audiosense.presentation.screens.home.components.CompactAudiogram
import ir.khebrati.audiosense.presentation.screens.home.components.HomeFAB
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinNavViewModel

@Composable
fun HomeScreen(
    onNavigateSelectDevice: (SelectDeviceRoute) -> Unit,
    onNavigateSetting: (SettingRoute) -> Unit,
    onNavigateCalibration: (CalibrationRoute) -> Unit,
    onNavigateResult: (ResultRoute) -> Unit,
    viewModel: HomeViewModel = koinNavViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenContent(uiState, onNavigateCalibration, onNavigateSelectDevice)
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    onNavigateCalibration: (CalibrationRoute) -> Unit,
    onNavigateSelectDevice: (SelectDeviceRoute) -> Unit,
) {
    AudiosenseScaffold(
        screenTitle = "Good ${uiState.currentTimeOfDay.capitalizedName()}",
        canNavigateBack = false,
        floatingActionButton = {
            HomeFAB(
                onNavigateCalibration = { onNavigateCalibration(CalibrationRoute) },
                onNavigateSelectDevice = { onNavigateSelectDevice(SelectDeviceRoute) },
            )
        },
        onNavigateBack = { /* No back navigation in Home */ },
    ) {
        TestRecordsList(uiState.testRecords)
    }
}

@Composable
fun TestRecordsList(testRecords: TestRecords) {
    when (testRecords) {
        is TestRecords.Loading -> LoadingTestRecords()
        is TestRecords.Ready -> {
            if (testRecords.compactTestRecordUiStates.isEmpty()) {
                EmptyRecordsList()
            } else RecordsList(testRecords)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingTestRecords() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularWavyProgressIndicator()
    }
}

@Composable
private fun RecordsList(testRecords: TestRecords.Ready) {
    LazyColumn {
        items(testRecords.compactTestRecordUiStates) { record ->
            SessionRecordCard(
                record.leftAC,
                record.rightAC,
                record.headphoneModel,
                record.lossDescription,
                date = record.date,
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun EmptyRecordsList() {
    Box(modifier = Modifier.fillMaxSize().padding(30.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            NoRecordsIcon()
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "No test records yet. Start a new one!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun SessionRecordCard(
    rightAC: Map<Int, Int>,
    leftAC: Map<Int, Int>,
    headphoneName: String,
    lossDescription: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    Card(
        colors =
            CardDefaults.cardColors()
                .copy(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        modifier = modifier.height(200.dp).fillMaxWidth(),
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(text = lossDescription, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = date, style = MaterialTheme.typography.labelMedium)
                }
                Box(modifier = Modifier.padding(5.dp)) {
                    CompactAudiogram(
                        rightAC = rightAC,
                        leftAC = leftAC,
                        modifier = Modifier.width(120.dp).height(90.dp),
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HeadphoneIcon(headphoneName, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = headphoneName, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun NoRecordsIcon() {
    Image(
        modifier = Modifier.size(100.dp),
        imageVector = vectorResource(Res.drawable.record_not_found),
        contentDescription = "No test records yet",
    )
}

@Preview
@Composable
fun EmptyHomeScreenPreview() {
    val emptyUiState =
        HomeUiState(
            currentTimeOfDay = TimeOfDay.AFTERNOON,
            testRecords = TestRecords.Ready(compactTestRecordUiStates = emptyList()),
        )
    AppTheme {
        HomeScreenContent(
            uiState = emptyUiState,
            onNavigateCalibration = {},
            onNavigateSelectDevice = {},
        )
    }
}

@Preview
@Composable
fun SessionRecordContentPreview() {
    AppTheme {
        SessionRecordCard(
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
            headphoneName = GalaxyBudsFE.model,
            lossDescription = "Mild Hearing Loss",
            date = "July 22, 2025",
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val uiState =
        HomeUiState(
            currentTimeOfDay = TimeOfDay.AFTERNOON,
            testRecords =
                TestRecords.Ready(
                    listOf(
                        CompactTestRecordUiState(
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
                            date = "July 22, 2025",
                            headphoneModel = GalaxyBudsFE.model,
                            lossDescription = "Profound loss",
                        ),
                        CompactTestRecordUiState(
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
                            date = "Feb 22, 2025",
                            headphoneModel = "Apple headphones",
                            lossDescription = "Normal hearing",
                        ),
                    )
                ),
        )
    AppTheme {
        HomeScreenContent(
            uiState = uiState,
            onNavigateCalibration = {},
            onNavigateSelectDevice = {},
        )
    }
}
