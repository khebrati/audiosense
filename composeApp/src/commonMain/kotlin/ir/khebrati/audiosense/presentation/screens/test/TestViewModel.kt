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
import ir.khebrati.audiosense.presentation.screens.test.TestUiAction.OnClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class TestViewModel(
    val handle: SavedStateHandle,
    val testRepository: TestRepository,
    val audiometry: PureToneAudiometry,
    val pcmGenerator: AudiometryPCMGenerator,
    val soundPlayer: SoundPlayer,
) :
    ViewModel() {
    val logger = Logger.withTag("TestViewModel")
    private val headphoneId = handle.toRoute<TestRoute>().selectedHeadphoneId

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState = _uiState.asStateFlow()

    init {
        keepStatesUpdated()
        startAudiometryProcedure()
    }

    private fun startAudiometryProcedure() {
        viewModelScope.launch {
            audiometry.start()
        }
        viewModelScope.launch(Dispatchers.Main) {
            audiometry.sounds.collect { soundPoint ->
                val duration = 1.seconds
                val pcm = pcmGenerator.generate(duration,soundPoint)
                soundPlayer.play(
                    samples = pcm,
                    duration = duration,
                    channel = _uiState.value.side.toSide().toAudioChannel()
                )
            }
        }
    }

    private fun keepStatesUpdated() {
        viewModelScope.launch {
            combineFlows().collect { progress ->
                logger.d { "Got progress $progress" }
                _uiState.update {
                    it.copy(progress = progress)
                }
            }
        }
    }

    fun combineFlows() = audiometry.progress

    val navigationEvents = MutableSharedFlow<NavigationEvent>()

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
