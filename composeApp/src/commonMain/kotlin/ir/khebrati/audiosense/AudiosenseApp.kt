package ir.khebrati.audiosense

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.presentation.components.AudiosenseAppBar
import ir.khebrati.audiosense.presentation.navigation.AudiosenseNavHost
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.Home
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.Koin

@Preview
@Composable
internal fun AudiosenseApp(koin: Koin) = AppTheme {
    val navController = rememberNavController()
//    val backStackEntryAsState = navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {
            AudiosenseAppBar(
                canNavigateBack = true,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(innerPadding),
        ) {
            AudiosenseNavHost( navController)
        }
    }
}









