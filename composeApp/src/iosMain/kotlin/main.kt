import androidx.compose.ui.window.ComposeUIViewController
import ir.khebrati.audiosense.AudiosenseApp
import ir.khebrati.audiosense.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { AudiosenseApp(initKoin()) }
