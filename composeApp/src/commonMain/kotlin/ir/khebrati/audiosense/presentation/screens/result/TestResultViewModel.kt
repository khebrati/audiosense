package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.domain.model.Side
import ir.khebrati.audiosense.domain.model.Test
import ir.khebrati.audiosense.domain.repository.TestRepository
import ir.khebrati.audiosense.domain.useCase.audiogram.AudiogramSerializer
import ir.khebrati.audiosense.domain.useCase.lossLevel.describeLossLevel
import ir.khebrati.audiosense.domain.useCase.lossLevel.getLossLevel
import ir.khebrati.audiosense.domain.useCase.share.ShareService
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.*
import ir.khebrati.audiosense.presentation.screens.result.TestResultIntent.*
import ir.khebrati.audiosense.presentation.screens.result.TestResultUiState.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TestResultViewModel(
    private val handle: SavedStateHandle,
    private val serializer: AudiogramSerializer,
    private val testRepository: TestRepository,
    private val shareService: ShareService,
) : ViewModel() {
    private val testId = handle.toRoute<ResultRoute>().testId
    val resultFlow = testRepository.observe(testId)
    val uiState: StateFlow<TestResultUiState> =
        resultFlow
            .map { result -> result.toUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Loading,
            )

    fun handleIntent(intent: TestResultIntent){
        when(intent){
            is ShareText -> handleShareText(intent)
            is ShareImage -> handleShareImage(intent)
        }
    }

    private fun handleShareImage(intent: ShareImage) {
        Logger.withTag("test").d { intent.bitmap.toString() }
    }

    private fun handleShareText(intent: ShareText) {
        val state = uiState.value
        if(state !is Ready){
            return
        }
        val serializedAudiogram = serializer.serialize(
            leftAC = state.leftAC,
            rightAC = state.rightAC
        )
        shareService.shareText(serializedAudiogram)
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
@Immutable
sealed interface TestResultIntent{
    data object ShareText : TestResultIntent
    data class ShareImage(val bitmap: ImageBitmap) : TestResultIntent
}


fun Test.toUiState() =
    Ready(
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
