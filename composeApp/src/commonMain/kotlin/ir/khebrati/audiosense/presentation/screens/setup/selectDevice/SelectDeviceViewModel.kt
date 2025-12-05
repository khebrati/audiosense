package ir.khebrati.audiosense.presentation.screens.setup.selectDevice

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.khebrati.audiosense.domain.model.Headphone
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import ir.khebrati.audiosense.presentation.screens.setup.selectDevice.SelectDeviceUiAction.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectDeviceViewModel(private val headphoneRepository: HeadphoneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SelectDeviceUiState())
    val uiState = _uiState.asStateFlow()

    private val headphones = headphoneRepository.observeAll().map { it.map { it.toUiState() } }

    init {
        viewModelScope.launch {
            headphones.collect { updatedHeadphones ->
                _uiState.update { SelectDeviceUiState(updatedHeadphones) }
            }
        }
    }

    fun handleAction(action: SelectDeviceUiAction) =
        when (action) {
            is SetSelectedDevice -> setSelectedDevice(action.index)
            is DeleteHeadphone -> deleteHeadphone(action.index)
        }

    private fun deleteHeadphone(index: Int) {
        viewModelScope.launch {
            val id = _uiState.value.headphones[index].id
            headphoneRepository.deleteById(id)
        }
    }

    fun setSelectedDevice(index: Int?) {
        _uiState.update { it.copy(selectedHeadphoneIndex = index) }
    }
}

@Immutable
sealed interface SelectDeviceUiAction {
    data class SetSelectedDevice(val index: Int?) : SelectDeviceUiAction

    data class DeleteHeadphone(val index: Int) : SelectDeviceUiAction
}

@Immutable
data class SelectDeviceUiState(
    val headphones: List<HeadphoneUiState> = emptyList(),
    val selectedHeadphoneIndex: Int? = null,
)

data class HeadphoneUiState(val model: String, val id: String)

fun Headphone.toUiState() = HeadphoneUiState(model, id)
