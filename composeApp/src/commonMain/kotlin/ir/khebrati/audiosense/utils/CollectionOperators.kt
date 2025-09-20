package ir.khebrati.audiosense.utils

/**
 * Creates a new map with the specified key and value added or updated.
 */
fun <T,R> Map<T,R>.copy(key: T, value: R): Map<T,R> {
    return this.toMutableMap().apply {
        this[key] = value
    }
}

fun <K : Comparable<K>, V> Map<out K, V>.toSortedMap(): Map<K, V>{
    val orderedKeys = this.keys.sorted()
    val mappedValue = orderedKeys.map { this[it] }
    return orderedKeys.zip(mappedValue).toMap() as Map<K, V>
}
