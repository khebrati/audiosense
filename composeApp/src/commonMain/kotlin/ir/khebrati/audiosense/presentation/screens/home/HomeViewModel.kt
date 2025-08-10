package ir.khebrati.audiosense.presentation.screens.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.khebrati.audiosense.domain.useCase.time.TimeOfDay
import ir.khebrati.audiosense.domain.useCase.time.TimeTeller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(val timeTeller: TimeTeller) : ViewModel() {
    private val currentTimeOfDay =
        timeTeller
            .observeTimeOfDay()
            .stateIn(
                viewModelScope,
                initialValue = timeTeller.tellTimeOfDay(),
                started = SharingStarted.WhileSubscribed(5000),
            )

    private val _uiState = MutableStateFlow(HomeUiState(currentTimeOfDay.value))
    val uiState = _uiState

    init {
        viewModelScope.launch {
            combineUiFlows().collect { timeOfDay -> _uiState.value = HomeUiState(timeOfDay) }
        }
    }

    private fun combineUiFlows() = currentTimeOfDay
}

@Immutable data class HomeUiState(val currentTimeOfDay: TimeOfDay)
