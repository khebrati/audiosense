package ir.khebrati.audiosense.presentation.screens.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.khebrati.audiosense.domain.model.Test
import ir.khebrati.audiosense.domain.repository.TestRepository
import ir.khebrati.audiosense.domain.useCase.lossLevel.describeLossLevel
import ir.khebrati.audiosense.domain.useCase.time.TimeOfDay
import ir.khebrati.audiosense.domain.useCase.time.TimeTeller
import ir.khebrati.audiosense.presentation.screens.home.HomeIntent.*
import kotlin.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeViewModel(val timeTeller: TimeTeller, val testRepository: TestRepository) : ViewModel() {
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
        MutableStateFlow(HomeUiState(currentTimeOfDay.value, TestHistory.Loading))
    val uiState = _uiState

    init {
        viewModelScope.launch { combineUiFlows().collect { state -> _uiState.update { state } } }
    }

    private fun combineUiFlows() =
        combine(currentTimeOfDay, testRecords) { currentTime, testRecords ->
            HomeUiState(currentTime, TestHistory.Ready(testRecords.map { it.toUiState() }))
        }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is SelectForDelete -> handleSelectForDelete(intent)
            is OnClick -> {
                /*Handled in UI for navigation*/
            }
            is CancelDelete -> handleCancelDelete(intent)
            is Delete -> handleDelete(intent)
        }
    }

    private fun handleDelete(intent: Delete) {
        val testHistory = _uiState.value.testHistory
        if(testHistory !is TestHistory.Ready){
            return
        }
        viewModelScope.launch {
            testHistory.compactRecords.filter { it.isSelectedForDelete }.forEach {
                testRepository.deleteById(it.id)
            }
        }
    }

    private fun handleCancelDelete(intent: CancelDelete) {
        val oldTestHistory = _uiState.value.testHistory
        if (oldTestHistory !is TestHistory.Ready) {
            return
        }
        val newCompactRecords =
            oldTestHistory.compactRecords.map { it.copy(isSelectedForDelete = false) }
        _uiState.update {
            it.copy(testHistory = oldTestHistory.copy(compactRecords = newCompactRecords))
        }
    }

    private fun handleSelectForDelete(intent: SelectForDelete) {
        val oldState = _uiState.value
        val oldTestRecords = oldState.testHistory
        val newTestRecord =
            if (oldTestRecords !is TestHistory.Ready) {
                oldTestRecords
            } else {
                val oldCompactTests = oldTestRecords.compactRecords
                val newCompactTests =
                    oldCompactTests.map {
                        if (it === intent.record)
                            it.copy(isSelectedForDelete = !it.isSelectedForDelete)
                        else it
                    }
                oldTestRecords.copy(compactRecords = newCompactTests)
            }
        val newState = oldState.copy(testHistory = newTestRecord)
        _uiState.update { newState }
    }
}

private fun Test.toUiState() =
    CompactRecord(
        id = id,
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

data class CompactRecord(
    val id: String,
    val leftAC: Map<Int, Int>,
    val rightAC: Map<Int, Int>,
    val date: String,
    val headphoneModel: String,
    val lossDescription: String,
    val isSelectedForDelete: Boolean = false,
)

@Immutable data class HomeUiState(val currentTimeOfDay: TimeOfDay, val testHistory: TestHistory)

sealed class TestHistory {
    data object Loading : TestHistory()

    data class Ready(val compactRecords: List<CompactRecord>) : TestHistory() {
        val isDelete = compactRecords.any { it.isSelectedForDelete }
        val deleteCount = compactRecords.count { it.isSelectedForDelete }
    }
}

@Immutable
sealed interface HomeIntent {
    data class SelectForDelete(val record: CompactRecord) : HomeIntent

    data class OnClick(val record: CompactRecord) : HomeIntent

    data object CancelDelete : HomeIntent

    data object Delete : HomeIntent
}
