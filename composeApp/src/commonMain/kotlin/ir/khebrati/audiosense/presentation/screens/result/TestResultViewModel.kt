package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.domain.model.Side
import ir.khebrati.audiosense.domain.model.Test
import ir.khebrati.audiosense.domain.repository.TestRepository
import ir.khebrati.audiosense.domain.useCase.lossLevel.describeLossLevel
import ir.khebrati.audiosense.domain.useCase.lossLevel.getLossLevel
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TestResultViewModel(
    private val handle: SavedStateHandle,
    private val testRepository: TestRepository,
) : ViewModel() {
    private val testId = handle.toRoute<ResultRoute>().testId
    val resultFlow = testRepository.observe(testId)
    val uiState: StateFlow<TestResultUiState> =
        resultFlow
            .map { result -> result.toUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TestResultUiState.Loading,
            )

    init {
        Logger.withTag("TestResultViewModel").d { "Got test id $testId" }
    }
}

@Immutable
sealed interface TestResultUiState {
    object Loading : TestResultUiState

    data class Ready(
        val generalLeftHearingLoss: Int,
        val generalRightHearingLoss: Int,
        val describedLeftHearingLoss: String,
        val describedRightHearingLoss: String,
        val leftAC: Map<Int, Int>,
        val rightAC: Map<Int, Int>,
    ) : TestResultUiState
}

fun Test.toUiState() =
    TestResultUiState.Ready(
        leftAC = leftAC,
        rightAC = rightAC,
        generalLeftHearingLoss = getLossLevel(leftAC),
        generalRightHearingLoss = getLossLevel(rightAC),
        describedLeftHearingLoss = describeLossLevel(leftAC),
        describedRightHearingLoss = describeLossLevel(rightAC)
    )

@Immutable
enum class SideUiState {
    LEFT,
    RIGHT,
}

fun Side.toUiState() =
    when (this) {
        Side.LEFT -> SideUiState.LEFT
        Side.RIGHT -> SideUiState.RIGHT
    }

fun SideUiState.toSide() =
    when (this) {
        SideUiState.LEFT -> Side.LEFT
        SideUiState.RIGHT -> Side.RIGHT
    }
