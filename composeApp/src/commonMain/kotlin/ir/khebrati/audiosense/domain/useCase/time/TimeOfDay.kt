package ir.khebrati.audiosense.domain.useCase.time

enum class TimeOfDay {
    MORNING,
    AFTERNOON,
    EVENING,
    NIGHT;
}
fun TimeOfDay.capitalizedName() = this.name.lowercase().replaceFirstChar { it.uppercase() }
