package ir.khebrati.audiosense.presentation.screens.calibration

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.domain.model.AcousticConstants.MAX_DB_SPL
import ir.khebrati.audiosense.domain.model.AcousticConstants.MIN_DB_SPL
import ir.khebrati.audiosense.domain.model.Side
import ir.khebrati.audiosense.domain.model.VolumeRecordPerFrequency
import ir.khebrati.audiosense.domain.repository.HeadphoneRepository
import ir.khebrati.audiosense.domain.useCase.calibrator.HeadphoneCalibrator
import ir.khebrati.audiosense.domain.useCase.sound.maker.test.TestSoundGenerator
import ir.khebrati.audiosense.domain.useCase.sound.player.SoundPlayer
import ir.khebrati.audiosense.domain.useCase.sound.player.toAudioChannel
import ir.khebrati.audiosense.domain.useCase.spl.fromDbSpl
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.PlaySound
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.Save
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SaveCalibrationUi
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetFrequency
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetMeasuredVolumeForCurrentFrequency
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetSide
import ir.khebrati.audiosense.presentation.screens.calibration.CalibrationUiAction.SetVolumeToPlayForCurrentFrequency
import ir.khebrati.audiosense.utils.copy
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalibrationViewModel(
    private val headphoneRepository: HeadphoneRepository,
    private val calibrator: HeadphoneCalibrator,
    private val testSoundGenerator: TestSoundGenerator,
    private val soundPlayer: SoundPlayer,
) : ViewModel() {
    private val frequencyOctaves = AcousticConstants.frequencyOctaves
    private val _selectedSide = MutableStateFlow(Side.LEFT)
    private val _selectedFrequency = MutableStateFlow(frequencyOctaves.first())
    private val _frequenciesVolumeData =
        MutableStateFlow(frequencyOctaves.associateWith { VolumeData() })

    private val currentVolumeToPlay =
        _selectedFrequency
            .combine(_frequenciesVolumeData) { frequency, frequenciesVolumeData ->
                frequenciesVolumeData[frequency]?.volumeToPlayDbSpl
                    ?: throw IllegalStateException("Frequency $frequency not found")
            }
            .stateIn(
                viewModelScope,
                initialValue = 50,
                started = SharingStarted.WhileSubscribed(5000),
            )

    private val currentMeasuredVolume =
        _selectedFrequency
            .combine(_frequenciesVolumeData) { frequency, frequenciesVolumeData ->
                frequenciesVolumeData[frequency]?.measuredVolumeDbSpl
                    ?: throw IllegalStateException("Frequency $frequency not found")
            }
            .stateIn(
                viewModelScope,
                initialValue = 50,
                started = SharingStarted.WhileSubscribed(5000),
            )

    private val _uiState = MutableStateFlow(CalibrationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { combineUiFlows().collect { _uiState.value = it } }
    }

    private fun combineUiFlows() =
        combine(_selectedFrequency, _selectedSide, currentVolumeToPlay, currentMeasuredVolume) {
            frequency,
            side,
            volumeToPlay,
            measuredVolume ->
            CalibrationUiState(
                frequency = frequency,
                volumeData = VolumeData(volumeToPlay, measuredVolume),
                side = side,
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
            is SetSide -> setSide(action.side)
        }
    }

    private fun setSide(side: Side) {
        _selectedSide.update { side }
    }

    private fun onSetFrequency(frequency: Int) {
        _selectedFrequency.value = frequency
    }

    private fun onSetPlayedVolumeForCurrentFrequency(volumeToPlayDbSpl: Int) {
        if (volumeToPlayDbSpl !in MIN_DB_SPL..MAX_DB_SPL) return
        val oldVolumeData = _frequenciesVolumeData.value[_selectedFrequency.value]
        val newVolumeData = oldVolumeData!!.copy(volumeToPlayDbSpl = volumeToPlayDbSpl)
        _frequenciesVolumeData.update { it.copy(_selectedFrequency.value, newVolumeData) }
    }

    private fun onSetMeasuredVolumeForCurrentFrequency(measuredVolumeDbSpl: Int) {
        val oldVolumeData = _frequenciesVolumeData.value[_selectedFrequency.value]
        val newVolumeData = oldVolumeData!!.copy(measuredVolumeDbSpl = measuredVolumeDbSpl)
        _frequenciesVolumeData.update { it.copy(_selectedFrequency.value, newVolumeData) }
    }

    private var saveJob: Job? = null

    private fun saveCalibration(headphoneModel: String) {
        if (saveJob?.isActive == true) return
        saveJob =
            viewModelScope.launch {
                val calibrationData = _frequenciesVolumeData.value.toModel()
                headphoneRepository.createHeadphone(headphoneModel, calibrationData)
            }
    }

    private fun playSound() {
        val soundSamples =
            testSoundGenerator.makeTestSound(
                frequency = _selectedFrequency.value,
                amplitude = currentVolumeToPlay.value.fromDbSpl(),
            )
        soundPlayer.play(samples = soundSamples, channel = _selectedSide.value.toAudioChannel())
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

    data class SetSide(val side: Side) : CalibrationUiAction
}

@Immutable
data class CalibrationUiState(
    val frequencies: List<Int> = AcousticConstants.frequencyOctaves,
    val frequency: Int = AcousticConstants.frequencyOctaves.first(),
    val volumeData: VolumeData = VolumeData(),
    val side: Side = Side.LEFT,
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
