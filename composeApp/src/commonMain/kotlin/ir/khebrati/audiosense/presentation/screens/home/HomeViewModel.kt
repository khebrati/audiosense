package ir.khebrati.audiosense.presentation.screens.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.khebrati.audiosense.domain.useCase.lossLevel.describeLossLevel
import ir.khebrati.audiosense.domain.useCase.time.TimeOfDay
import ir.khebrati.audiosense.domain.useCase.time.TimeTeller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    // todo load from db
    private val testRecords =
        MutableStateFlow(
            listOf(
                CompactTestRecordUiState(
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
                    date = "July 22, 2025",
                    headphoneModel = "Galaxy buds fe",
                    lossDescription = describeLossLevel(14)
                ),
                CompactTestRecordUiState(
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
                    date = "Feb 22, 2025",
                    headphoneModel = "Apple headphones",
                    lossDescription = describeLossLevel(50)
                ),
            )
        )

    private val _uiState = MutableStateFlow(HomeUiState(currentTimeOfDay.value, testRecords.value))
    val uiState = _uiState

    init {
        viewModelScope.launch { combineUiFlows().collect { state -> _uiState.update { state } } }
    }

    private fun combineUiFlows() =
        combine(currentTimeOfDay, testRecords) { currentTime, testRecords ->
            HomeUiState(currentTime, testRecords)
        }
}

data class CompactTestRecordUiState(
    val leftAC: Map<Int, Int>,
    val rightAC: Map<Int, Int>,
    val date: String,
    val headphoneModel: String,
    val lossDescription: String,
)

@Immutable
data class HomeUiState(
    val currentTimeOfDay: TimeOfDay,
    val compactTestRecordUiStates: List<CompactTestRecordUiState>,
)
