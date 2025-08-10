package ir.khebrati.audiosense.domain.useCase.time

import kotlinx.coroutines.flow.Flow

interface TimeTeller {
    /**
     * Returns the current time of day.
     */
    fun tellTimeOfDay() : TimeOfDay
    fun observeTimeOfDay() : Flow<TimeOfDay>
}