package ir.khebrati.audiosense.presentation.screens.calibration

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import ir.khebrati.audiosense.domain.model.GlobalConstants
import ir.khebrati.audiosense.domain.useCase.calibrator.HeadphoneCalibrator
import ir.khebrati.audiosense.domain.model.VolumeRecordPerFrequency
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.PlaySound
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.Save
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SaveCalibrationUi
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetFrequency
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetMeasuredVolumeForCurrentFrequency
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetVolumeToPlayForCurrentFrequency
import ir.khebrati.audiosense.utils.copy
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalibrationViewModel(
    private val headphoneRepository: HeadphoneRepository,
    private val calibrator: HeadphoneCalibrator,
) : ViewModel() {
    val frequencyOctaves = GlobalConstants.frequencyOctaves
    private val _selectedFrequency = MutableStateFlow(frequencyOctaves.first())
    private val _frequenciesVolumeData =
        MutableStateFlow(frequencyOctaves.associateWith { VolumeData() })

    private val _uiState = MutableStateFlow(CalibrationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { combineUiFlows().collect { _uiState.value = it } }
    }

    private fun combineUiFlows() =
        combine(_selectedFrequency, _frequenciesVolumeData) { frequency, frequenciesVolumeData ->
            CalibrationUiState(
                frequency = frequency,
                volumeToPlay =
                    frequenciesVolumeData[frequency]?.volumeToPlayDbSpl
                        ?: throw IllegalStateException("Frequency $frequency not found"),
                measuredVolume =
                    frequenciesVolumeData[frequency]?.measuredVolumeDbSpl
                        ?: throw IllegalStateException("Frequency $frequency not found"),
            )
        }

    fun onUiAction(action: CalibrationUiAction) {
        when (action) {
            is SaveCalibrationUi -> saveCalibration(action.headphoneModel)
            is SetFrequency -> onSetFrequency(action.frequency)
            is SetVolumeToPlayForCurrentFrequency ->
                onSetPlayedVolumeForCurrentFrequency(action.playedVolumeDbSpl)
            is SetMeasuredVolumeForCurrentFrequency ->
                onSetMeasuredVolumeForCurrentFrequency(action.measuredVolumeDbSpl)
            is Save -> saveCalibration(action.headphoneModel)
            is PlaySound -> playSound()
        }
    }

    private fun onSetFrequency(frequency: Int) {
        _selectedFrequency.value = frequency
    }

    private fun onSetPlayedVolumeForCurrentFrequency(playedVolumeDbSpl: Int) {
        val oldVolumeData = _frequenciesVolumeData.value[_selectedFrequency.value]
        val newVolumeData = oldVolumeData!!.copy(volumeToPlayDbSpl = playedVolumeDbSpl)
        _frequenciesVolumeData.update { it.copy(_selectedFrequency.value, newVolumeData) }
    }

    private fun onSetMeasuredVolumeForCurrentFrequency(measuredVolumeDbSpl: Int) {
        val oldVolumeData = _frequenciesVolumeData.value[_selectedFrequency.value]
        val newVolumeData = oldVolumeData!!.copy(measuredVolumeDbSpl = measuredVolumeDbSpl)
        _frequenciesVolumeData.update { it.copy(_selectedFrequency.value, newVolumeData) }
    }

    private var saveJob: Job? = null

    fun saveCalibration(headphoneModel: String) {
        if (saveJob?.isActive == true) return
        saveJob =
            viewModelScope.launch {
                val calibrationData =
                    _frequenciesVolumeData.value.toModel()
                headphoneRepository.createHeadphone(headphoneModel, calibrationData)
            }
    }

    fun playSound() {
        // TODO
    }
}

@Immutable
sealed interface CalibrationUiAction {
    data class SaveCalibrationUi(val headphoneModel: String) : CalibrationUiAction

    data class SetFrequency(val frequency: Int) : CalibrationUiAction

    data class SetVolumeToPlayForCurrentFrequency(val playedVolumeDbSpl: Int) : CalibrationUiAction

    data class SetMeasuredVolumeForCurrentFrequency(val measuredVolumeDbSpl: Int) :
        CalibrationUiAction

    data object PlaySound : CalibrationUiAction

    data class Save(val headphoneModel: String) : CalibrationUiAction
}

@Immutable
data class CalibrationUiState(
    val frequencies: List<Int> = GlobalConstants.frequencyOctaves,
    val frequency: Int = GlobalConstants.frequencyOctaves.first(),
    val volumeToPlay: Int = 50,
    val measuredVolume: Int = 50,
)

@Immutable
data class VolumeData(val volumeToPlayDbSpl: Int = 50, val measuredVolumeDbSpl: Int = 50)

fun Map<Int, VolumeData>.toModel() =
    this.mapValues {
        VolumeRecordPerFrequency(
            playedVolumeDbSpl = it.value.volumeToPlayDbSpl,
            measuredVolumeDbSpl = it.value.measuredVolumeDbSpl,
        )
    }
