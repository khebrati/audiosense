package ir.khebrati.audiosense.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.CalibrationRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResultRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.HomeRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.NoiseMeterRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.ResultRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDeviceRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SettingRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.TestRoute
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationScreen
import ir.khebrati.audiosense.presentation.screens.descriptiveResult.DescriptiveResultScreen
import ir.khebrati.audiosense.presentation.screens.home.HomeScreen
import ir.khebrati.audiosense.presentation.screens.result.ResultScreen
import ir.khebrati.audiosense.presentation.screens.setting.SettingScreen
import ir.khebrati.audiosense.presentation.screens.test.TestScreen
import ir.khebrati.audiosense.presentation.screens.testPreparation.noiseMeter.NoiseMeterScreen
import ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice.SelectDeviceScreen

@Composable
fun AudiosenseNavHost(navController: NavHostController) {
    NavHost(startDestination = HomeRoute, navController = navController) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateSelectDevice = { navController.navigate(it) },
                onNavigateSetting = { navController.navigate(it) },
                onNavigateResult = { navController.navigate(it) },
            )
        }
        composable<SelectDeviceRoute> { backStackEntry ->
            val selectDeviceRoute: SelectDeviceRoute = backStackEntry.toRoute()
            SelectDeviceScreen(
                selectDeviceRoute = selectDeviceRoute,
                onNavigateTest = { navController.navigate(it) },
                onNavigateBack = { navController.popBackStack() },
                onNavigateCalibration = {navController.navigate(it)}
            )
        }
        composable<NoiseMeterRoute> { backStackEntry ->
            val noiseMeterRoute: NoiseMeterRoute = backStackEntry.toRoute()
            NoiseMeterScreen(
                noiseMeterRoute = noiseMeterRoute,
                onNavigateTest = { navController.navigate(it) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<TestRoute> { backStackEntry ->
            val testRoute: TestRoute = backStackEntry.toRoute()
            TestScreen(
                testRoute = testRoute,
                onNavigateResult = { navController.navigate(it) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<ResultRoute> { backStackEntry ->
            val resultRoute: ResultRoute = backStackEntry.toRoute()
            ResultScreen(
                resultRoute = resultRoute,
                onNavigateHome = { navController.popBackStack<HomeRoute>(inclusive = false) },
                onNavigateDescriptiveResult = { navController.navigate(it) },
            )
        }
        composable<DescriptiveResultRoute> { backStackEntry ->
            val descriptiveResultRoute: DescriptiveResultRoute = backStackEntry.toRoute()
            DescriptiveResultScreen(
                descriptiveResultRoute = descriptiveResultRoute,
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<CalibrationRoute> { backStackEntry ->
            val calibrationRoute: CalibrationRoute = backStackEntry.toRoute()
            CalibrationScreen(
                calibrationRoute = calibrationRoute,
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<SettingRoute> { backStackEntry ->
            val settingRoute: SettingRoute = backStackEntry.toRoute()
            SettingScreen(
                settingRoute = settingRoute,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
