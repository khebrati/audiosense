package ir.khebrati.audiosense.presentation.screens.home.components

// import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import ir.khebrati.audiosense.presentation.components.M3ExpressiveFAB
import ir.khebrati.audiosense.presentation.components.M3ExpressiveMenuItem
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeFAB(
    modifier: Modifier = Modifier,
    onNavigateCalibration: () -> Unit,
    onNavigateSelectDevice: () -> Unit,
) {
    val items =
        listOf(
            M3ExpressiveMenuItem(
                text = { Text("New Test") },
                icon = { Icon(Icons.Default.Science, contentDescription = null) },
                onClick = onNavigateSelectDevice,
            ),
            M3ExpressiveMenuItem(
                text = { Text("Calibrate") },
                icon = { Icon(Icons.Default.Adjust, contentDescription = null) },
                onClick = onNavigateCalibration
            ),
        )
    M3ExpressiveFAB(items = items, modifier = modifier)
}

@Preview
@Composable
fun PreviewHomeFAB() {
//    AppTheme { HomeFAB() }
}
