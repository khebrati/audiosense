package ir.khebrati.audiosense.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import audiosense.composeapp.generated.resources.Res
import audiosense.composeapp.generated.resources.record_not_found
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.domain.model.DefaultHeadphones.GalaxyBudsFE
import ir.khebrati.audiosense.domain.useCase.time.TimeOfDay
import ir.khebrati.audiosense.domain.useCase.time.capitalizedName
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.components.HeadphoneIcon
import ir.khebrati.audiosense.presentation.components.LoadingScreen
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.ResultRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDeviceRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SettingRoute
import ir.khebrati.audiosense.presentation.screens.home.HomeIntent.OnClick
import ir.khebrati.audiosense.presentation.screens.home.HomeIntent.SelectForDelete
import ir.khebrati.audiosense.presentation.screens.home.components.CompactAudiogram
import ir.khebrati.audiosense.presentation.screens.home.components.HomeFAB
import ir.khebrati.audiosense.presentation.screens.home.components.SelectableCheckbox
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinNavViewModel

@Composable
fun HomeScreen(
    onNavigateSelectDevice: (SelectDeviceRoute) -> Unit,
    onNavigateSetting: (SettingRoute) -> Unit,
    onNavigateResult: (ResultRoute) -> Unit,
    viewModel: HomeViewModel = koinNavViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val intentHandler: (HomeIntent) -> Unit = { intent ->
        when (intent) {
            is OnClick -> onNavigateResult(ResultRoute(intent.record.id))
            else -> viewModel.handleIntent(intent)
        }
    }
    HomeScreenContent(uiState, intentHandler, onNavigateSelectDevice)
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    onNavigateSelectDevice: (SelectDeviceRoute) -> Unit,
) {
    val testHistory = uiState.testHistory
    val leftPadding = if(testHistory is TestHistory.Ready && testHistory.isDelete) 0.dp else 25.dp
    AudiosenseScaffold(
        screenTitle = "Good ${uiState.currentTimeOfDay.capitalizedName()}",
        canNavigateBack = false,
        contentPadding = PaddingValues(end = 25.dp, start = leftPadding, top = 25.dp, bottom = 25.dp),
        floatingActionButton = {
            HomeFAB(onNavigateSelectDevice = { onNavigateSelectDevice(SelectDeviceRoute) })
        },
        onNavigateBack = { /* No back navigation in Home */ },
    ) {
        TestRecordsList(testHistory, onIntent = onIntent)
    }
}

@Composable
fun TestRecordsList(testHistory: TestHistory, onIntent: (HomeIntent) -> Unit) {
    when (testHistory) {
        is TestHistory.Loading -> LoadingScreen()
        is TestHistory.Ready -> {
            if (testHistory.compactRecords.isEmpty()) {
                EmptyRecordsList()
            } else RecordsList(testHistory, onIntent = onIntent)
        }
    }
}

@Composable
private fun RecordsList(testHistory: TestHistory.Ready, onIntent: (HomeIntent) -> Unit) {
    LazyColumn {
        items(testHistory.compactRecords) { record ->
            Row(
                modifier =
                    Modifier.fillMaxWidth()
                        .height(200.dp)
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (testHistory.isDelete) onIntent(SelectForDelete(record))
                                else onIntent(OnClick(record))
                            },
                            onLongClick = { onIntent(SelectForDelete(record)) },
                        )
            ) {
                if (testHistory.isDelete) {
                    CheckboxArea(
                        modifier = Modifier.weight(1f),
                        selected = record.isSelectedForDelete,
                    )
                }
                SessionRecordCard(
                    modifier = Modifier.weight(8f),
                    rightAC = record.leftAC,
                    leftAC = record.rightAC,
                    headphoneName = record.headphoneModel,
                    lossDescription = record.lossDescription,
                    date = record.date,
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun CheckboxArea(modifier: Modifier = Modifier, selected: Boolean) {
    Box(modifier = modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
        SelectableCheckbox(Modifier.size(20.dp), selected, {})
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
        modifier = modifier.fillMaxHeight(),
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
            testHistory = TestHistory.Ready(compactRecords = emptyList()),
        )
    AppTheme {
        HomeScreenContent(uiState = emptyUiState, onNavigateSelectDevice = {}, onIntent = {})
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
            modifier = Modifier.height(200.dp),
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val uiState =
        HomeUiState(
            currentTimeOfDay = TimeOfDay.AFTERNOON,
            testHistory =
                TestHistory.Ready(
                    listOf(
                        CompactRecord(
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
                            id = "23",
                        ),
                        CompactRecord(
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
                            id = "232",
                        ),
                    )
                ),
        )
    AppTheme { HomeScreenContent(uiState = uiState, onNavigateSelectDevice = {}, onIntent = {}) }
}
