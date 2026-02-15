package ir.khebrati.audiosense.di

import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.data.repository.HeadphoneRepositoryImpl
import ir.khebrati.audiosense.data.repository.TestRepositoryImpl
import ir.khebrati.audiosense.data.source.local.createDatabase
import ir.khebrati.audiosense.data.source.local.createSqlDriver
import ir.khebrati.audiosense.data.source.remote.HeadphoneFetcher
import ir.khebrati.audiosense.data.source.remote.HeadphoneFetcherImpl
import ir.khebrati.audiosense.data.source.remote.TokenManager
import ir.khebrati.audiosense.db.AudiosenseDb
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import ir.khebrati.audiosense.domain.repository.TestRepository
import ir.khebrati.audiosense.domain.useCase.audiogram.AudiogramSerializer
import ir.khebrati.audiosense.domain.useCase.audiogram.AudiogramSerializerImpl
import ir.khebrati.audiosense.domain.useCase.audiometry.PureToneAudiometry
import ir.khebrati.audiosense.domain.useCase.audiometry.PureToneAudiometryImpl
import ir.khebrati.audiosense.domain.useCase.calibrator.HeadphoneCalibrator
import ir.khebrati.audiosense.domain.useCase.calibrator.HeadphoneCalibratorImpl
import ir.khebrati.audiosense.domain.useCase.sound.maker.harmonic.HarmonicGenerator
import ir.khebrati.audiosense.domain.useCase.sound.maker.harmonic.HarmonicGeneratorImpl
import ir.khebrati.audiosense.domain.useCase.sound.maker.test.AudiometryPCMGenerator
import ir.khebrati.audiosense.domain.useCase.sound.maker.test.AudiometryPCMGeneratorImpl
import ir.khebrati.audiosense.domain.useCase.time.TimeTeller
import ir.khebrati.audiosense.domain.useCase.time.TimeTellerImpl
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationViewModel
import ir.khebrati.audiosense.presentation.screens.home.HomeViewModel
import ir.khebrati.audiosense.presentation.screens.result.TestResultViewModel
import ir.khebrati.audiosense.presentation.screens.setup.personal.PersonalInfoViewModel
import ir.khebrati.audiosense.presentation.screens.setup.selectDevice.SelectDeviceViewModel
import ir.khebrati.audiosense.presentation.screens.test.TestViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

internal fun commonModule(): Module = module {
    // Database
    single<AudiosenseDb> { createDatabase(createSqlDriver()) }

    // Repository
    single<HeadphoneRepository> {
        HeadphoneRepositoryImpl(
            database = get(),
            headphoneFetcher = get(),
            dispatcher = Dispatchers.Default
        )
    }
    single<TestRepository> {
        TestRepositoryImpl(
            database = get(),
            dispatcher = Dispatchers.Default
        )
    }
    // View Model
    viewModelOf(::CalibrationViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::PersonalInfoViewModel)
    viewModelOf(::TestViewModel)
    viewModelOf(::SelectDeviceViewModel)
    viewModelOf(::TestResultViewModel)
    //UseCase
    single<TokenManager> { TokenManager() }
    single<HeadphoneFetcher> {
        HeadphoneFetcherImpl(tokenManager = get())
    }
    factory<HeadphoneCalibrator>{
        HeadphoneCalibratorImpl()
    }
    factory<HarmonicGenerator>{
        HarmonicGeneratorImpl()
    }
    factory<AudiometryPCMGenerator>{
        AudiometryPCMGeneratorImpl(get())
    }
    factory<TimeTeller>{
        TimeTellerImpl()
    }
    factory<PureToneAudiometry>{
        PureToneAudiometryImpl(logger = get{ parametersOf("PureToneAudiometry") })
    }
    factory<AudiogramSerializer>{
        AudiogramSerializerImpl()
    }
}

