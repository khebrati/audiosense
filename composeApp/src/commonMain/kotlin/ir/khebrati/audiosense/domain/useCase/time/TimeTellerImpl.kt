package ir.khebrati.audiosense.domain.useCase.time

import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TimeTellerImpl : TimeTeller {
    /** @return Morning, Afternoon, Evening or Night based on the current time. */
    override fun tellTimeOfDay(): TimeOfDay {
        val currentHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
        return when (currentHour) {
            in 5..11 -> TimeOfDay.MORNING
            in 12..17 -> TimeOfDay.AFTERNOON
            in 18..22 -> TimeOfDay.EVENING
            else -> TimeOfDay.NIGHT
        }
    }

    override fun observeTimeOfDay() =
        flow {
                while (true) {
                    emit(tellTimeOfDay())
                    delay(1.minutes)
                }
            }
            .distinctUntilChanged()
}
