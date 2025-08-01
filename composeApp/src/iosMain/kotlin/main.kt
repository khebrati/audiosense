import androidx.compose.ui.window.ComposeUIViewController
import ir.khebrati.audiosense.AudiosenseApp
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { AudiosenseApp() }
