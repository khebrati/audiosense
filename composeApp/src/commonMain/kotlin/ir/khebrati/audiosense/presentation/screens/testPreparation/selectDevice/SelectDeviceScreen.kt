package ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Earbuds
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.NoiseMeter
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDevice
import ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice.SelectDeviceUiAction.SetSelectedDevice
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinNavViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectDeviceScreen(
    selectDeviceRoute: SelectDevice,
    onNavigateNoiseMeter: (NoiseMeter) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SelectDeviceViewModel = koinNavViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    BackHandler{
        onNavigateBack()
    }
    AudiosenseScaffold(
        screenTitle = selectDeviceRoute.title,
        canNavigateBack = true,
        onNavigateBack = onNavigateBack,
    ) {
        SelectDeviceContent(
            uiState = uiState,
            onUiAction = viewModel::handleAction,
            onNavigateNoiseMeter = onNavigateNoiseMeter
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HeadphonesList(
    headphoneNames: List<String>,
    selectedIndex: Int? = null,
    onSelectedChange: (Int) -> Unit,
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
            HeadphoneItem(
                modelName = name,
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
                        HeadphoneUiState(model = "Galaxy buds", id = "0"),
                        HeadphoneUiState(model = "Sony Headphones", id = "1"),
                        HeadphoneUiState(model = "Airpods", id = "2"),
                    )
            )
        )
    }
    AppTheme {
        AudiosenseScaffold(screenTitle = "New Test", canNavigateBack = true, onNavigateBack = {}) {
            SelectDeviceContent(uiState, {},{})
        }
    }
}

@Composable
private fun SelectDeviceContent(
    uiState: SelectDeviceUiState,
    onUiAction: (SelectDeviceUiAction) -> Unit,
    onNavigateNoiseMeter: (NoiseMeter) -> Unit,
) {
    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
        HeadphonesList(
            uiState.headphones.map { it.model },
            uiState.selectedHeadphoneIndex,
            onSelectedChange = { onUiAction(SetSelectedDevice(it)) },
        )
        NextButton(onClick = {onNavigateNoiseMeter(NoiseMeter)})
    }
}

@Composable
private fun NextButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(modifier = Modifier.fillMaxWidth().height(60.dp), onClick = onClick) { Text("Next") }
}

@Composable
fun HeadphoneItem(
    modelName: String,
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
            Box(
                modifier =
                    Modifier.background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = MaterialTheme.shapes.medium,
                        )
                        .size(50.dp),
                contentAlignment = Alignment.Center,
            ) {
                val icon =
                    if (isEarbuds(modelName)) Icons.Default.Earbuds else Icons.Default.Headphones
                Icon(
                    contentDescription = "Headphone icon",
                    imageVector = icon,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
            Text(text = modelName, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

private fun isEarbuds(name: String) = name.lowercase().contains(Regex("bud|buds|airpod|airpods"))

@Preview(showBackground = true)
@Composable
fun HeadphoneItemPreview() {
    AppTheme {
        HeadphoneItem(
            modelName = "Galaxy Buds",
            modifier = Modifier.fillMaxWidth(),
            isSelected = true,
        )
    }
}
