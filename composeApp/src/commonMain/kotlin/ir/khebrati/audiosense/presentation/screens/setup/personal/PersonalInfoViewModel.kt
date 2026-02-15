package ir.khebrati.audiosense.presentation.screens.setup.personal

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.khebrati.audiosense.domain.repository.TestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalInfoViewModel(private val testRepository: TestRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonalInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadLastTestInfo()
    }

    private fun loadLastTestInfo() {
        viewModelScope.launch {
            val lastTest = testRepository.getLastTest()
            if (lastTest != null) {
                _uiState.update {
                    it.copy(
                        initialAge = lastTest.personAge.toString(),
                        initialName = lastTest.personName ?: "",
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

@Immutable
data class PersonalInfoUiState(
    val initialAge: String = "",
    val initialName: String = "",
    val isLoading: Boolean = true
)

