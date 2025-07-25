import androidx.compose.ui.window.ComposeUIViewController
import ir.khebrati.audiosense.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
