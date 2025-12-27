package ir.khebrati.audiosense.data.source.remote

import ir.khebrati.audiosense.data.source.local.entity.LocalTest

interface AudiogramPoster {
    suspend fun postAudiogram(localTest: LocalTest): Boolean
}
