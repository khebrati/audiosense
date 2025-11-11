package ir.khebrati.audiosense.di

import AudioSense.composeApp.BuildConfig
import co.touchlab.kermit.BaseLogger
import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.koin.kermitLoggerModule
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(
    nativePlatformModule: Module? = null,
): Koin {
    return startKoin {
        modules(
            listOfNotNull(
                nativePlatformModule,
                platformModule(),
                commonModule(),
                kermitModule
            )
        )
    }.koin
}

val kermitModule = module {
    factory<Logger> { (tag: String) ->
        Logger(
            loggerConfigInit(
                platformLogWriter(NoTagFormatter),
                minSeverity = if(BuildConfig.IS_DEVELOPMENT) Severity.Verbose else Severity.Info,
            ),
            tag
        )
    }
}