package ir.khebrati.audiosense.presentation.screens.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.khebrati.audiosense.domain.model.Test
import ir.khebrati.audiosense.domain.repository.TestRepository
import ir.khebrati.audiosense.domain.useCase.lossLevel.describeLossLevel
import ir.khebrati.audiosense.domain.useCase.time.TimeOfDay
import ir.khebrati.audiosense.domain.useCase.time.TimeTeller
import kotlin.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeViewModel(val timeTeller: TimeTeller, testRepository: TestRepository) : ViewModel() {
    private val currentTimeOfDay =
        timeTeller
            .observeTimeOfDay()
            .stateIn(
                viewModelScope,
                initialValue = timeTeller.tellTimeOfDay(),
                started = SharingStarted.WhileSubscribed(5000),
            )

    private val testRecords = testRepository.observeAll()

    private val _uiState =
        MutableStateFlow(HomeUiState(currentTimeOfDay.value, TestRecords.Loading))
    val uiState = _uiState

    init {
        viewModelScope.launch { combineUiFlows().collect { state -> _uiState.update { state } } }
    }

    private fun combineUiFlows() =
        combine(currentTimeOfDay, testRecords) { currentTime, testRecords ->
            HomeUiState(currentTime, TestRecords.Ready(testRecords.map { it.toUiState() }))
        }
}

private fun Test.toUiState() =
    CompactTestRecordUiState(
        leftAC = leftAC,
        rightAC = rightAC,
        date = toHumanReadableDate(dateTime),
        headphoneModel = headphone.model,
        lossDescription = describeLossLevel(leftAC, rightAC),
    )

private fun toHumanReadableDate(instant: Instant): String {
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDate.date.month} ${localDate.day}, ${localDate.year}"
}

data class CompactTestRecordUiState(
    val leftAC: Map<Int, Int>,
    val rightAC: Map<Int, Int>,
    val date: String,
    val headphoneModel: String,
    val lossDescription: String,
)

@Immutable data class HomeUiState(val currentTimeOfDay: TimeOfDay, val testRecords: TestRecords)

sealed class TestRecords {
    data object Loading : TestRecords()

    data class Ready(val compactTestRecordUiStates: List<CompactTestRecordUiState>) : TestRecords()
}
