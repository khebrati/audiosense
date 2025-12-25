package ir.khebrati.audiosense.data.source.remote

import AudioSense.composeApp.BuildConfig
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import ir.khebrati.audiosense.data.source.remote.entity.RemoteHeadphone
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonTransformingSerializer

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

class HeadphoneFetcherImpl : HeadphoneFetcher {
    override suspend fun fetchAllFromServer(): List<RemoteHeadphone> {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.post("http://188.121.121.80:3000/api/login") {
            contentType(ContentType.Application.Json)
            val body = LoginRequest(username = BuildConfig.API_USERNAME, password = BuildConfig.API_PASSWORD)
            val serializedBody = Json{}.encodeToString(
                body
            )
            setBody(serializedBody)
        }
        Logger.d{response.bodyAsText()}
        return emptyList()
    }
}