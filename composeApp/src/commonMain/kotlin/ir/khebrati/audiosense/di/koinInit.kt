package ir.khebrati.audiosense.di

import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun koinInit(
    platformModule: Module,
    nativePlatformModule: Module,
    commonModule: Module,
): Koin {
    return startKoin {
        modules(
            listOf(
                platformModule,
                nativePlatformModule,
                commonModule
            )
        )
    }.koin
}