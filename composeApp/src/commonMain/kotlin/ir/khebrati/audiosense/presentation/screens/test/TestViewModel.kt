package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.domain.repository.TestRepository
import ir.khebrati.audiosense.domain.useCase.audiometry.PureToneAudiometry
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
) : ViewModel() {
    val navigationEvents = MutableSharedFlow<NavigationEvent>()
    val logger = Logger.withTag("TestViewModel")
    private val headphoneId = handle.toRoute<TestRoute>().selectedHeadphoneId

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState = _uiState.asStateFlow()

    init {
        keepStatesUpdated()
        startAudiometryProcedure()
    }

    private fun startAudiometryProcedure() {
        audiometry.performActionWhenFinished { leftAC, rightAC -> save(leftAC, rightAC) }
        viewModelScope.launch { audiometry.start() }
        viewModelScope.launch {
            audiometry.soundToPlay.collect { soundPoint ->
                val duration = 1.seconds
                val pcm = pcmGenerator.generate(duration, soundPoint)
                soundPlayer.play(
                    samples = pcm,
                    duration = duration,
                    channel = _uiState.value.side.toSide().toAudioChannel(),
                )
            }
        }
    }

    private fun save(leftAC: Map<Int, Int>, rightAC: Map<Int, Int>) {
        viewModelScope.launch {
            val testId = testRepository.createTest(
                dateTime = Clock.System.now(),
                // TODO measure and save noise
                noiseDuringTest = 0,
                leftAC = leftAC,
                rightAC = rightAC,
                headphoneId = headphoneId,
            )
            navigationEvents.emit(NavigateToResult(ResultRoute(testId)))
        }
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
