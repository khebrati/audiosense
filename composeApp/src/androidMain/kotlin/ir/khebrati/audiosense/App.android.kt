package ir.khebrati.audiosense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ir.khebrati.audiosense.di.initKoin
import org.koin.dsl.module

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val nativePlatformModule = module {
            single { this@AppActivity }
            single<android.content.Context> {
                this@AppActivity.application.applicationContext
            }
        }
        val koin = initKoin(nativePlatformModule = nativePlatformModule)
        setContent { AudiosenseApp(koin) }
    }
}
