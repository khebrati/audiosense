package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.domain.model.Side
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TestResultViewModel(
    private val handle: SavedStateHandle
) : ViewModel() {
    val _uiState = MutableStateFlow(
        TestResultUiState(
            averageLeftHearingLossDBHL = 46,
            averageRightHearingLossDBHL = 30,
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
        )
    )
    val uiState = _uiState.asStateFlow()
    private val testId = handle.toRoute<ResultRoute>()
    init {
        Logger.withTag("TestResultViewModel").d { "Got test id $testId" }
    }
}

@Immutable
data class TestResultUiState(
    val averageLeftHearingLossDBHL: Int,
    val averageRightHearingLossDBHL: Int,
    val leftAC: Map<Int, Int>,
    val rightAC: Map<Int, Int>,
)

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
