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
import kotlin.time.Clock
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TestViewModel(
    val handle: SavedStateHandle,
    val testRepository: TestRepository,
    val audiometry: PureToneAudiometry,
    val pcmGenerator: AudiometryPCMGenerator,
    val soundPlayer: SoundPlayer
) :
    ViewModel() {
    private val headphoneId = handle.toRoute<TestRoute>().selectedHeadphoneId

    init {
        Logger.withTag("TestViewModel").d { "Got device id $headphoneId" }
        keepStatesUpdated()
        startAudiometryProcedure()
    }

    private fun startAudiometryProcedure() {
        viewModelScope.launch {
            audiometry.start()
            audiometry.sounds.collect { soundPoint ->
                val pcm = pcmGenerator.generate(soundPoint)
                soundPlayer.play(
                    samples = pcm,
                    channel = _uiState.value.side.toSide().toAudioChannel()
                )
            }
        }
    }

    private fun keepStatesUpdated() {
        viewModelScope.launch {
            combineFlows().collect { progress ->
                _uiState.update {
                    it.copy(progress = progress)
                }
            }
        }
    }

    fun combineFlows() = audiometry.progress

    val navigationEvents = MutableSharedFlow<NavigationEvent>()
    private val _uiState = MutableStateFlow(TestUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(uiAction: TestUiAction) {
        when (uiAction) {
            is OnClick -> handleClick()
        }
    }

    fun handleClick() {
        audiometry.onHeard()
    }

    private fun insertFakeTestToDb() {
        viewModelScope.launch {
            val testId =
                testRepository.createTest(
                    dateTime = Clock.System.now(),
                    noiseDuringTest = 30,
                    leftAC =
                        hashMapOf(
                            125 to 90,
                            250 to 50,
                            500 to 20,
                            1000 to 20,
                            2000 to 30,
                            4000 to 55,
                            8000 to 35,
                        ),
                    rightAC =
                        hashMapOf(
                            125 to 30,
                            250 to 30,
                            500 to 5,
                            1000 to 0,
                            2000 to 0,
                            4000 to 5,
                            8000 to 5,
                        ),
                    headphoneId = headphoneId,
                )
            navigationEvents.emit(NavigationEvent.NavigateToResult(ResultRoute(testId)))
        }
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
