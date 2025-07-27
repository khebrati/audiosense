package ir.khebrati.audiosense.data.source.local

import androidx.room.TypeConverter
import kotlin.time.Instant

class Convertors {
    @TypeConverter
    fun mapToString(map: Map<Int, Int>): String {
        return map.entries.joinToString(separator = ",") { "${it.key}:${it.value}" }
    }

    @TypeConverter
    fun stringToMap(value: String): Map<Int, Int> {
        return value.split(",").filter { it.isNotBlank() }.associate {
            val (key, value) = it.split(":").map {
                it.toIntOrNull()
                    ?: throw IllegalArgumentException("$value can not be converted to map of <Int,Int>")
            }
            key to value
        }
    }

    @TypeConverter
    fun instantToString(instant: Instant) : String {
        return instant.toString()
    }

    @TypeConverter
    fun stringToInstant(value: String) : Instant {
        return Instant.parse(value)
    }
}