package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import ir.khebrati.audiosense.domain.repository.TestRepository
import ir.khebrati.audiosense.domain.useCase.audiometry.PureToneAudiometry
import ir.khebrati.audiosense.domain.useCase.audiometry.toSoundPoint
import ir.khebrati.audiosense.domain.useCase.sound.maker.test.AudiometryPCMGenerator
import ir.khebrati.audiosense.domain.useCase.sound.player.SoundPlayer
import ir.khebrati.audiosense.domain.useCase.sound.player.toAudioChannel
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.result.SideUiState
import ir.khebrati.audiosense.presentation.screens.result.toSide
import ir.khebrati.audiosense.presentation.screens.result.toUiState
import ir.khebrati.audiosense.presentation.screens.test.NavigationEvent.*
import ir.khebrati.audiosense.presentation.screens.test.TestUiAction.OnClick
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TestViewModel(
    val handle: SavedStateHandle,
    val testRepository: TestRepository,
    val audiometry: PureToneAudiometry,
    val pcmGenerator: AudiometryPCMGenerator,
    val soundPlayer: SoundPlayer,
    val headphoneRepository: HeadphoneRepository,
) : ViewModel() {
    val navigationEvents = MutableSharedFlow<NavigationEvent>()
    val logger = Logger.withTag("TestViewModel")
    private val testRoute = handle.toRoute<TestRoute>()
    private val headphoneId = testRoute.selectedHeadphoneId
    private val personName = testRoute.personName
    private val personAge = testRoute.personAge
    private val hasHearingAidExperience = testRoute.hasHearingAidExperience

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState = _uiState.asStateFlow()

    init {
        keepStatesUpdated()
        startAudiometryProcedure()
    }

    private fun startAudiometryProcedure() {
        audiometry.performActionWhenFinished { leftAC, rightAC ->
            logger.d { "Received ac from operation : $leftAC $rightAC" }
            viewModelScope.launch { save(calibrate(leftAC), calibrate(rightAC)) }
        }
        viewModelScope.launch { audiometry.start() }
        viewModelScope.launch {
            audiometry.soundToPlay.collect { dbPoint ->
                val duration = 1.seconds
                val pcm = pcmGenerator.generate(duration, dbPoint.toSoundPoint())
                soundPlayer.play(
                    samples = pcm,
                    duration = duration,
                    channel = _uiState.value.side.toSide().toAudioChannel(),
                )
            }
        }
    }

    private suspend fun calibrate(ac: Map<Int, Int>): Map<Int, Int> {
        val usedHeadphone =
            headphoneRepository.getById(headphoneId)
                ?: throw IllegalStateException("Headphone with id $headphoneId does not exist")
        logger.i { "Applying coeff: ${usedHeadphone.calibrationCoefficients}" }
        val rawCalibrated = ac.mapValues { it.value + (usedHeadphone.calibrationCoefficients[it.key] ?: 0) }
        val minPossibleDb = AcousticConstants.MIN_DB_SPL
        val maxPossibleDb = AcousticConstants.MAX_DB_SPL
        val calibratedInBounds = rawCalibrated.mapValues { it.value.coerceIn(minPossibleDb,maxPossibleDb) }
        logger.v { "Calibrated ac: $calibratedInBounds" }
        return calibratedInBounds
    }

    private suspend fun save(leftAC: Map<Int, Int>, rightAC: Map<Int, Int>) {
        val testId =
            testRepository.createTest(
                dateTime = Clock.System.now(),
                // TODO measure and save noise
                noiseDuringTest = 0,
                leftAC = leftAC,
                rightAC = rightAC,
                headphoneId = headphoneId,
                personName = personName,
                personAge = personAge,
                hasHearingAidExperience = hasHearingAidExperience,
            )
        navigationEvents.emit(NavigateToResult(ResultRoute(testId)))
    }

    private fun keepStatesUpdated() {
        viewModelScope.launch {
            combineFlows().collect { newState -> _uiState.update { newState } }
        }
    }

    fun combineFlows() =
        combine(audiometry.progress, audiometry.currentSide) { progress, currentSide ->
            TestUiState(progress, currentSide.toUiState())
        }

    fun onUiAction(uiAction: TestUiAction) {
        when (uiAction) {
            is OnClick -> handleClick()
        }
    }

    fun handleClick() {
        audiometry.onHeard()
    }
}

@Immutable
data class TestUiState(val progress: Float = 0f, val side: SideUiState = SideUiState.LEFT)

@Immutable
sealed interface TestUiAction {
    data object OnClick : TestUiAction
}

@Immutable
sealed interface NavigationEvent {
    data class NavigateToResult(val route: ResultRoute) : NavigationEvent
}
