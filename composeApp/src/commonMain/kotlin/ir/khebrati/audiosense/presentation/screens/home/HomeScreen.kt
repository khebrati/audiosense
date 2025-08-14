package ir.khebrati.audiosense.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.khebrati.audiosense.domain.useCase.time.capitalizedName
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Calibration
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Results
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDevice
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Setting
import ir.khebrati.audiosense.presentation.screens.home.components.HomeFAB
import org.koin.compose.viewmodel.koinNavViewModel

@Composable
fun HomeScreen(
    onNavigateSelectDevice: (SelectDevice) -> Unit,
    onNavigateSetting: (Setting) -> Unit,
    onNavigateCalibration: (Calibration) -> Unit,
    onNavigateResult: (Results) -> Unit,
    viewModel: HomeViewModel = koinNavViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AudiosenseScaffold(
        screenTitle = "Good ${uiState.currentTimeOfDay.capitalizedName()}",
        canNavigateBack = false,
        floatingActionButton = {
            HomeFAB(
                onNavigateCalibration = {onNavigateCalibration(Calibration)},
                onNavigateSelectDevice = {onNavigateSelectDevice(SelectDevice)}
            )
        },
        onNavigateBack = { /* No back navigation in Home */ },
    ){

    }
}
