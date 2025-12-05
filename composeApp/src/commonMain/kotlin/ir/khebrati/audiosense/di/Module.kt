package ir.khebrati.audiosense.di

import androidx.room.RoomDatabase
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.data.repository.HeadphoneRepositoryImpl
import ir.khebrati.audiosense.data.repository.TestRepositoryImpl
import ir.khebrati.audiosense.data.source.local.AppDatabase
import ir.khebrati.audiosense.data.source.local.dao.HeadphoneDao
import ir.khebrati.audiosense.data.source.local.dao.TestDao
import ir.khebrati.audiosense.data.source.local.dao.TestHeadphoneDao
import ir.khebrati.audiosense.data.source.local.getRoomDatabase
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
import ir.khebrati.audiosense.presentation.screens.test.TestViewModel
import ir.khebrati.audiosense.presentation.screens.setup.selectDevice.SelectDeviceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

internal fun commonModule(): Module = module {
    // Database
    single<AppDatabase> { getRoomDatabase(get<RoomDatabase.Builder<AppDatabase>>()) }
    // Dao
    single<HeadphoneDao> { get<AppDatabase>().headphoneDao() }
    single<TestDao> { get<AppDatabase>().testDao() }
    single<TestHeadphoneDao> { get<AppDatabase>().testHeadphoneDao() }
    // Repository
    single<HeadphoneRepository> {
        HeadphoneRepositoryImpl(headphoneDao = get(), dispatcher = Dispatchers.IO)
    }
    single<TestRepository> {
        TestRepositoryImpl(testDao = get(), testHeadphoneDao = get(), dispatcher = Dispatchers.IO)
    }
    // View Model
    viewModelOf(::CalibrationViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::TestViewModel)
    viewModelOf(::SelectDeviceViewModel)
    viewModelOf(::TestResultViewModel)
    //UseCase
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

expect fun platformModule(): Module
