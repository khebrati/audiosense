package ir.khebrati.audiosense.data.source.local

import androidx.room.TypeConverter
import kotlin.time.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Convertors {
    @TypeConverter
    fun mapPairToString(map: Map<Int, Pair<Int, Int>>) : String =
        getJsonSerializer().encodeToString<Map<Int, Pair<Int, Int>>>(map)

    @TypeConverter
    fun stringToMapPair(value: String): Map<Int, Pair<Int, Int>> =
        getJsonSerializer().decodeFromString<Map<Int, Pair<Int, Int>>>(value)

    @TypeConverter
    fun stringToMap(value: String): Map<Int, Int> =
        getJsonSerializer().decodeFromString<Map<Int, Int>>(value)

    @TypeConverter
    fun mapToString(map: Map<Int,Int>): String =
        getJsonSerializer().encodeToString<Map<Int,Int>>(map)

    @TypeConverter
    fun instantToString(instant: Instant): String {
        return instant.toString()
    }

    @TypeConverter
    fun stringToInstant(value: String): Instant {
        return Instant.parse(value)
    }

    private fun getJsonSerializer() = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
}
