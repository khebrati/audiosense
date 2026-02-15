package ir.khebrati.audiosense.domain.useCase.audiometry

import ir.khebrati.audiosense.domain.model.Side
import ir.khebrati.audiosense.domain.model.SoundPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface PureToneAudiometry {
    /**
     * Goes from 0f to 1f, which means audiometry is done.
     */
    val progress : StateFlow<Float>
    val currentSide: StateFlow<Side>
    val soundToPlay: SharedFlow<DbPoint>
    suspend fun start()

    /**
     * Should be called when patient hears a [SoundPoint] emitted by [start]
     */
    fun onHeard()

    /**
     * @param action The lambda that is called when test results are ready. It receives left and right AC Maps.
     */
    fun performActionWhenFinished(action: (Map<Int, Int>, Map<Int, Int>) -> Unit)
}