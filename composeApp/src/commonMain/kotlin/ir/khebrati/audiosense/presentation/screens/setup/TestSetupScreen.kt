package ir.khebrati.audiosense.presentation.screens.setup

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.setup.navigation.SetupInternalRoute.*
import ir.khebrati.audiosense.presentation.screens.setup.personal.PersonalInfoScreen
import ir.khebrati.audiosense.presentation.screens.setup.selectDevice.SelectDeviceScreen
import ir.khebrati.audiosense.presentation.screens.setup.tap.TapScreen
import ir.khebrati.audiosense.presentation.screens.setup.volume.VolumeSettingScreen
import kotlinx.coroutines.launch


@Composable
fun TestSetupScreen(
    modifier: Modifier = Modifier,
    route: TestSetupRoute,
    onNavigateHome: (HomeRoute) -> Unit,
    onNavigateTest: (TestRoute) -> Unit,
    onNavigateCalibration: (CalibrationRoute) -> Unit
) {
    val pagerState = rememberPagerState { 4 }
    val currentPage = pagerState.currentPage
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    fun navigateBack() {
        scope.launch { pagerState.animateScrollToPage(currentPage - 1) }
        navController.popBackStack()
    }
    NavHost(startDestination = PersonalInfoRoute, navController = navController) {
        composable<PersonalInfoRoute> { backStackEntry ->
            val personalInfoRoute: PersonalInfoRoute = backStackEntry.toRoute()
            PersonalInfoScreen(
                personalInfoRoute = personalInfoRoute,
                onNavigateVolume = { navController.navigate(it) },
                onNavigateBack = { onNavigateHome(HomeRoute) },
                pagerState = pagerState,
                onClickSkip = {
                    // TODO implement
                },
            )
        }
        composable<VolumeRoute>{backStackEntry ->
            val volumeRoute : VolumeRoute = backStackEntry.toRoute()
            VolumeSettingScreen(
                volumeRoute = volumeRoute,
                pagerState = pagerState,
                onNavigateTap = {navController.navigate(it)},
                onNavigateBack = {navigateBack()}
            )
        }
        composable<TapRoute>{ backStackEntry ->
            val tapRoute : TapRoute = backStackEntry.toRoute()
            TapScreen(
                tapRoute = tapRoute,
                onNavigateSelectDevice = {navController.navigate(it)},
                pagerState = pagerState,
                onNavigateBack = {navigateBack()}
            )
        }
        composable<SelectDeviceRoute>{backStackEntry ->
            val selectDeviceRoute : SelectDeviceRoute = backStackEntry.toRoute()
            SelectDeviceScreen(
                selectDeviceRoute = selectDeviceRoute,
                onNavigateTest = onNavigateTest,
                onNavigateBack = {navigateBack()},
                onNavigateCalibration = onNavigateCalibration
            )
        }
    }
}
