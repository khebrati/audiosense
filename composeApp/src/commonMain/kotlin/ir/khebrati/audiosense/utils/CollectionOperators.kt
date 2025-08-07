package ir.khebrati.audiosense.utils

/**
 * Creates a new map with the specified key and value added or updated.
 */
fun <T,R> Map<T,R>.copy(key: T, value: R): Map<T,R> {
    return this.toMutableMap().apply {
        this[key] = value
    }
}