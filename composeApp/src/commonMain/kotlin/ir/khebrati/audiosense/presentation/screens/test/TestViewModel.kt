package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.TestRoute
import ir.khebrati.audiosense.presentation.screens.test.TestUiAction.OnClick
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestViewModel(
    val handle: SavedStateHandle,
) : ViewModel() {
    private val selectedDeviceId = handle.toRoute<TestRoute>().selectedDeviceId
    init {
        Logger.withTag("TestViewModel").d { "Got device id $selectedDeviceId" }
        //todo perform actual testing
    }
    private val _uiState = MutableStateFlow(TestUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(uiAction: TestUiAction) {
        when (uiAction) {
            is OnClick -> handleClick()
        }
    }

    fun handleClick() {
        _uiState.update {
            it.copy(it.progress + 0.1f)
        }
    }
}

@Immutable data class TestUiState(val progress: Float = 0f)

@Immutable
sealed interface TestUiAction {
    data object OnClick : TestUiAction
}
