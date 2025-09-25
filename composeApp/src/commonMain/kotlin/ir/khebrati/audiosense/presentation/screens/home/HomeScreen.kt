package ir.khebrati.audiosense.presentation.screens.home

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    ) {}
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
            headphoneName = "Galaxy buds FE",
            lossDescription = "Mild Hearing Loss",
            date = "July 22, 2025",
        )
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
        Column(modifier = Modifier.fillMaxSize().padding(15.dp)) {
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
                Box(
                    modifier = Modifier.padding(5.dp)
                ) {
                    CompactAudiogram(
                        rightAC = rightAC,
                        leftAC = leftAC,
                        modifier = Modifier.width(150.dp).height(120.dp),
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.width(110.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HeadphoneIcon(headphoneName, modifier = Modifier.size(15.dp))
                    Text(text = headphoneName, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    AudiosenseScaffold(
        screenTitle = "Good morning",
        canNavigateBack = false,
        floatingActionButton = { HomeFAB(onNavigateCalibration = {}, onNavigateSelectDevice = {}) },
        onNavigateBack = {},
    ) {}
    AppTheme {}
}
