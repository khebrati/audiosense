package ir.khebrati.audiosense

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ir.khebrati.audiosense.presentation.navigation.AudiosenseNavHost
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.Koin

@Preview
@Composable
internal fun AudiosenseApp(koin: Koin) = AppTheme {
    val navController = rememberNavController()
    AudiosenseNavHost(
        navController = navController,
    )
}









