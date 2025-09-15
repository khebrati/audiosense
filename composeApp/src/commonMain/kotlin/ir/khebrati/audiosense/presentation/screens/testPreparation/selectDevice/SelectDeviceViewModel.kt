package ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.khebrati.audiosense.domain.model.Headphone
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import ir.khebrati.audiosense.presentation.screens.testPreparation.selectDevice.SelectDeviceUiAction.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectDeviceViewModel(private val headphoneRepository: HeadphoneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SelectDeviceUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHeadphones()
    }

    fun loadHeadphones() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(headphones = headphoneRepository.getAll().map { it.toUiState() })
            }
        }
    }

    fun handleAction(action: SelectDeviceUiAction) =
        when (action) {
            is SetSelectedDevice -> setSelectedDevice(action.index)
        }

    fun setSelectedDevice(index: Int?) {
        _uiState.update { it.copy(selectedHeadphoneIndex = index) }
    }
}

@Immutable
sealed interface SelectDeviceUiAction {
    data class SetSelectedDevice(val index: Int?) : SelectDeviceUiAction
}

@Immutable
data class SelectDeviceUiState(
    val headphones: List<HeadphoneUiState> = emptyList(),
    val selectedHeadphoneIndex: Int? = null,
)

data class HeadphoneUiState(val model: String, val id: String)

fun Headphone.toUiState() = HeadphoneUiState(model, id)
