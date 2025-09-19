package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import ir.khebrati.audiosense.domain.model.Side

class TestResultViewModel : ViewModel() {}

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
