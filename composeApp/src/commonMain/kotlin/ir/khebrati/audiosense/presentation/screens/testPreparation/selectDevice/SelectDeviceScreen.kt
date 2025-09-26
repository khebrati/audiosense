package ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice

// import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import audiosense.composeapp.generated.resources.AppleAirpodPro
import audiosense.composeapp.generated.resources.GalaxyBudsFe
import audiosense.composeapp.generated.resources.Res
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.components.HeadphoneIcon
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice.SelectDeviceUiAction.SetSelectedDevice
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinNavViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectDeviceScreen(
    selectDeviceRoute: SelectDeviceRoute,
    onNavigateTest: (TestRoute) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateCalibration: (CalibrationRoute) -> Unit,
    viewModel: SelectDeviceViewModel = koinNavViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    // TODO commented to build previews
    //    BackHandler { onNavigateBack() }
    AudiosenseScaffold(
        screenTitle = selectDeviceRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack,
    ) {
        SelectDeviceContent(
            uiState = uiState,
            onUiAction = viewModel::handleAction,
            onNavigateTest = onNavigateTest,
            onNavigateCalibration = onNavigateCalibration
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HeadphonesList(
    headphoneNames: List<String>,
    selectedIndex: Int? = null,
    onSelectedChange: (Int) -> Unit,
    onNavigateCalibration: (CalibrationRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(
                modifier = modifier.fillMaxWidth().height(63.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Select your device",
                    style = MaterialTheme.typography.titleLargeEmphasized,
                )
            }
        }
        itemsIndexed(headphoneNames) { index, name ->
            ListItem(
                text = name,
                onClick = { onSelectedChange(index) },
                isSelected = selectedIndex == index,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectDevicePreview() {
    var uiState by remember {
        mutableStateOf(
            SelectDeviceUiState(
                headphones =
                    listOf(
                        HeadphoneUiState(model = "Galaxy Buds FE", id = "0"),
                        HeadphoneUiState(model = "Apple Airpods", id = "2"),
                        HeadphoneUiState(model = "Sony Headphones", id = "1"),
                    )
            )
        )
    }
    AppTheme {
        AudiosenseScaffold(screenTitle = "New Test", canNavigateBack = true, onNavigateBack = {}) {
            SelectDeviceContent(uiState, {}, {},{})
        }
    }
}

@Composable
private fun SelectDeviceContent(
    uiState: SelectDeviceUiState,
    onUiAction: (SelectDeviceUiAction) -> Unit,
    onNavigateTest: (TestRoute) -> Unit,
    onNavigateCalibration: (CalibrationRoute) -> Unit,
) {
    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
        Scaffold(
            modifier = Modifier.weight(1f),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onNavigateCalibration(CalibrationRoute)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add a new headphone"
                    )
                }
            },
        ) {
            HeadphonesList(
                uiState.headphones.map { it.model },
                uiState.selectedHeadphoneIndex,
                onSelectedChange = { onUiAction(SetSelectedDevice(it)) },
                onNavigateCalibration = onNavigateCalibration
            )
        }
        val selectedHeadphoneId =
            remember(uiState) {
                uiState.run {
                    if (selectedHeadphoneIndex == null) null
                    else headphones[selectedHeadphoneIndex].id
                }
            }
        NextButton(
            enabled = selectedHeadphoneId != null,
            onClick = { onNavigateTest(TestRoute(selectedHeadphoneId!!)) },
        )
    }
}

@Composable
private fun NextButton(onClick: () -> Unit, enabled: Boolean, modifier: Modifier = Modifier) {
    Button(modifier = modifier.fillMaxWidth().height(60.dp), onClick = onClick, enabled = enabled) {
        Text("Next")
    }
}

@Composable
fun ListItem(
    text: String,
    onClick: () -> Unit = {},
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
        modifier =
            if (isSelected)
                Modifier.clickable(interactionSource = null, indication = null) { onClick() }
                    .border(
                        border = BorderStroke(3.dp, color = MaterialTheme.colorScheme.primary),
                        shape = MaterialTheme.shapes.medium,
                    )
            else Modifier.clickable { onClick() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = modifier.padding(12.dp).fillMaxWidth(),
        ) {
            when (text) {
                "Galaxy Buds FE" -> {
                    Image(
                        painter = painterResource(Res.drawable.GalaxyBudsFe),
                        contentDescription = "Galaxy Buds FE image",
                        modifier = Modifier.size(70.dp),
                    )
                }
                "Apple Airpods" -> {
                    Image(
                        painter = painterResource(Res.drawable.AppleAirpodPro),
                        contentDescription = "Apple Airpods",
                        modifier = Modifier.size(70.dp),
                    )
                }
                else -> {
                    Box(modifier = Modifier.size(70.dp), contentAlignment = Alignment.Center) {
                        Box(
                            modifier =
                                Modifier.background(
                                        color = MaterialTheme.colorScheme.tertiaryContainer,
                                        shape = MaterialTheme.shapes.medium,
                                    )
                                    .size(50.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            HeadphoneIcon(text)
                        }
                    }
                }
            }
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AddNewIcon(modifier: Modifier = Modifier) {
    Icon(
        contentDescription = "Headphone icon",
        imageVector = Icons.Default.Add,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun HeadphoneItemPreview() {
    AppTheme {
        ListItem(text = "Galaxy Buds", modifier = Modifier.fillMaxWidth(), isSelected = true)
    }
}
