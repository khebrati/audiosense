package ir.khebrati.audiosense.di

import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initKoin(
    nativePlatformModule: Module? = null,
): Koin {
    return startKoin {
        modules(
            listOfNotNull(
                platformModule(),
                nativePlatformModule,
                commonModule()
            )
        )
    }.koin
}