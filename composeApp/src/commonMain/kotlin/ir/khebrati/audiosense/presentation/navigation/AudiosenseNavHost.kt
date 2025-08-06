package ir.khebrati.audiosense.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Calibration
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResult
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Home
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.NoiseMeter
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Results
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.SelectDevice
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Setting
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Test
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
    NavHost(startDestination = Home, navController = navController) {
        composable<Home> {
            HomeScreen(
                onNavigateSelectDevice = { navController.navigate(it) },
                onNavigateSetting = { navController.navigate(it) },
                onNavigateCalibration = { navController.navigate(it) },
                onNavigateResult = { navController.navigate(it) },
            )
        }
        composable<SelectDevice> { backStackEntry ->
            val selectDeviceRoute: SelectDevice = backStackEntry.toRoute()
            SelectDeviceScreen(
                selectDeviceRoute = selectDeviceRoute,
                onNavigateNoiseMeter = { navController.navigate(it) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<NoiseMeter> { backStackEntry ->
            val noiseMeterRoute: NoiseMeter = backStackEntry.toRoute()
            NoiseMeterScreen(
                noiseMeterRoute = noiseMeterRoute,
                onNavigateTest = { navController.navigate(it) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<Test> { backStackEntry ->
            val testRoute: Test = backStackEntry.toRoute()
            TestScreen(
                testRoute = testRoute,
                onNavigateResult = { navController.navigate(it) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<Results> { backStackEntry ->
            val resultRoute: Results = backStackEntry.toRoute()
            ResultScreen(
                resultRoute = resultRoute,
                onNavigateBack = { navController.popBackStack<Home>(inclusive = false) },
                onNavigateDescriptiveResult = { navController.navigate(it) },
            )
        }
        composable<DescriptiveResult> { backStackEntry ->
            val descriptiveResultRoute: DescriptiveResult = backStackEntry.toRoute()
            DescriptiveResultScreen(
                descriptiveResultRoute = descriptiveResultRoute,
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<Calibration> { backStackEntry ->
            val calibrationRoute: Calibration = backStackEntry.toRoute()
            CalibrationScreen(
                calibrationRoute = calibrationRoute,
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<Setting> { backStackEntry ->
            val settingRoute: Setting = backStackEntry.toRoute()
            SettingScreen(
                settingRoute = settingRoute,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
