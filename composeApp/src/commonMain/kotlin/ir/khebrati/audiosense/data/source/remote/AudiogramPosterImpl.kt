package ir.khebrati.audiosense.data.source.remote

import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.data.source.local.entity.LocalTest
import ir.khebrati.audiosense.data.source.remote.entity.RemoteTest
import ir.khebrati.audiosense.data.toRemote
import kotlinx.serialization.Serializable


class AudiogramPosterImpl(private val authHandler: RemoteAuthHandler) : AudiogramPoster {

    override suspend fun postAudiogram(localTest: LocalTest): Boolean {
        val remoteTest = localTest.toRemote()
        val body = authHandler.json.encodeToString(RemoteTest.serializer(), remoteTest)

        return authHandler.authenticatedPost(
            endpoint = "/audiogram",
            body = body,
            onSuccess = {
                true
            },
            onInvalidCredentials = {
                false
            },
            onError = { error ->
                Logger.e { "Failed to post audiogram: $error" }
                false
            }
        )
    }
}
