package ir.khebrati.audiosense.data.source.remote

import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.data.source.remote.entity.RemoteHeadphone
import kotlinx.serialization.Serializable

@Serializable
data class HeadphonesResponse(
    val success: Boolean,
    val headphones: List<RemoteHeadphone> = emptyList(),
    val error: String? = null,
)

class HeadphoneFetcherImpl(private val authHandler: RemoteAuthHandler) : HeadphoneFetcher {

    override suspend fun fetchAllFromServer(): List<RemoteHeadphone> {
        return authHandler.authenticatedGet(
            endpoint = "/headphones",
            onSuccess = { responseText ->
                val headphonesResponse = authHandler.json.decodeFromString<HeadphonesResponse>(responseText)
                if (headphonesResponse.success) {
                    headphonesResponse.headphones
                } else {
                    Logger.e { "Failed to fetch headphones: ${headphonesResponse.error}" }
                    emptyList()
                }
            },
            onInvalidCredentials = {
                emptyList()
            },
            onError = { error ->
                Logger.e { "Error fetching headphones: $error" }
                emptyList()
            }
        )
    }
}

