import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import ir.khebrati.audiosense.AudiosenseApp
import ir.khebrati.audiosense.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() = ComposeViewport { AudiosenseApp(initKoin()) }
