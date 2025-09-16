package ir.khebrati.audiosense.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.khebrati.audiosense.domain.useCase.time.capitalizedName
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.CalibrationRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.ResultsRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDeviceRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SettingRoute
import ir.khebrati.audiosense.presentation.screens.home.components.HomeFAB
import org.koin.compose.viewmodel.koinNavViewModel

@Composable
fun HomeScreen(
    onNavigateSelectDevice: (SelectDeviceRoute) -> Unit,
    onNavigateSetting: (SettingRoute) -> Unit,
    onNavigateCalibration: (CalibrationRoute) -> Unit,
    onNavigateResult: (ResultsRoute) -> Unit,
    viewModel: HomeViewModel = koinNavViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AudiosenseScaffold(
        screenTitle = "Good ${uiState.currentTimeOfDay.capitalizedName()}",
        canNavigateBack = false,
        floatingActionButton = {
            HomeFAB(
                onNavigateCalibration = {onNavigateCalibration(CalibrationRoute)},
                onNavigateSelectDevice = {onNavigateSelectDevice(SelectDeviceRoute)}
            )
        },
        onNavigateBack = { /* No back navigation in Home */ },
    ){

    }
}
