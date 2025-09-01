package ir.khebrati.audiosense.presentation.screens.test

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import ir.khebrati.audiosense.presentation.screens.test.TestUiAction.OnClick
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestViewModel() : ViewModel() {

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
