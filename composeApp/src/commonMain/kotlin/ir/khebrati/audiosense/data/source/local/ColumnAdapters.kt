package ir.khebrati.audiosense.data.source.local

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Instant
import kotlin.Int
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object MapIntIntAdapter : ColumnAdapter<Map<Int, Int>, String> {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    override fun decode(databaseValue: String): Map<Int, Int> {
        return json.decodeFromString(databaseValue)
    }

    override fun encode(value: Map<Int, Int>): String {
        return json.encodeToString(value)
    }
}

object InstantAdapter : ColumnAdapter<Instant, String> {
    override fun decode(databaseValue: String): Instant {
        return Instant.parse(databaseValue)
    }

    override fun encode(value: Instant): String {
        return value.toString()
    }
}


