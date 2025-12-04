package ir.khebrati.audiosense.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationScreen
import ir.khebrati.audiosense.presentation.screens.descriptiveResult.DescriptiveResultScreen
import ir.khebrati.audiosense.presentation.screens.home.HomeScreen
import ir.khebrati.audiosense.presentation.screens.result.ResultScreen
import ir.khebrati.audiosense.presentation.screens.setting.SettingScreen
import ir.khebrati.audiosense.presentation.screens.test.TestScreen
import ir.khebrati.audiosense.presentation.screens.testPreparation.personal.PersonalInfoScreen
import ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice.SelectDeviceScreen
import ir.khebrati.audiosense.presentation.screens.testPreparation.tap.TapScreen
import ir.khebrati.audiosense.presentation.screens.testPreparation.volume.VolumeSettingScreen

@Composable
fun AudiosenseNavHost(navController: NavHostController) {
    NavHost(startDestination = HomeRoute, navController = navController) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateStartTest = { navController.navigate(it) },
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
        composable<PersonalInfoRoute> { backStackEntry ->
            val noiseMeterRoute: PersonalInfoRoute = backStackEntry.toRoute()
            PersonalInfoScreen(
                personalInfoRoute = noiseMeterRoute,
                onNavigateVolume = { navController.navigate(it) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<VolumeRoute> { backStackEntry ->
            val volumeRoute: VolumeRoute = backStackEntry.toRoute()
            VolumeSettingScreen(
                volumeRoute = volumeRoute,
                onNavigateTap = {navController.navigate(it)},
                onNavigateBack = {navController.popBackStack()}
            )
        }
        composable<TapRoute> { backStackEntry ->
            val tapRoute: TapRoute = backStackEntry.toRoute()
            TapScreen(
                tapRoute = tapRoute,
                onNavigateSelectDevice = {navController.navigate(it)},
                onNavigateBack = {navController.popBackStack()}
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
