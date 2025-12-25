package ir.khebrati.audiosense.data.source.remote

import AudioSense.composeApp.BuildConfig
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import ir.khebrati.audiosense.data.source.remote.entity.RemoteHeadphone
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val BASE_URL = "http://188.121.121.80:3000/api"

@Serializable data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val expiresIn: String? = null,
    val error: String? = null,
)

@Serializable
data class HeadphonesResponse(
    val success: Boolean,
    val headphones: List<RemoteHeadphone> = emptyList(),
    val error: String? = null,
)

class HeadphoneFetcherImpl(private val tokenManager: TokenManager) : HeadphoneFetcher {

    private val client = HttpClient(CIO)
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun fetchAllFromServer(): List<RemoteHeadphone> {
        // Try to get valid token, or login if needed
        val token = getValidToken() ?: return emptyList()

        return fetchHeadphones(token)
    }

    private suspend fun getValidToken(): String? {
        // Return existing valid token
        tokenManager.getToken()?.let {
            return it
        }

        // Need to login
        return login()
    }

    private suspend fun login(): String {
        val response: HttpResponse =
            client.post("$BASE_URL/login") {
                contentType(ContentType.Application.Json)
                val body =
                    LoginRequest(
                        username = BuildConfig.API_USERNAME,
                        password = BuildConfig.API_PASSWORD,
                    )
                setBody(json.encodeToString(LoginRequest.serializer(), body))
            }

        val responseText = response.bodyAsText()
        Logger.d { "Login response: $responseText" }

        val loginResponse = json.decodeFromString<LoginResponse>(responseText)

        if (loginResponse.success && loginResponse.token != null) {
            // Parse expiresIn (e.g., "24h") and store token
            val hours = parseExpiresIn(loginResponse.expiresIn)
            tokenManager.setToken(loginResponse.token, hours)
            return loginResponse.token
        } else {
            throw Exception("Login failed")
        }
    }

    private suspend fun fetchHeadphones(token: String): List<RemoteHeadphone> {
        val response: HttpResponse =
            client.get("$BASE_URL/headphones") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

        val responseText = response.bodyAsText()
        Logger.d { "Headphones response: $responseText" }

        // Check if token is invalid
        if (!response.status.isSuccess()) {
            val errorResponse = json.decodeFromString<LoginResponse>(responseText)

            if (errorResponse.error == "Invalid credentials") {
                // Token expired, clear it and retry login
                tokenManager.clearToken()
                val newToken = login()
                return fetchHeadphones(newToken)
            }
            errorResponse.error?.let { Logger.e { it } }
            return emptyList()
        }

        val headphonesResponse = json.decodeFromString<HeadphonesResponse>(responseText)
        if (headphonesResponse.success) {
            return headphonesResponse.headphones
        } else {
            throw Exception("Failed to fetch headphones: ${headphonesResponse.error}")
        }
    }

    private fun parseExpiresIn(expiresIn: String?): Int {
        if (expiresIn == null) return 24
        // Parse formats like "24h", "1h", etc.
        return expiresIn.replace("h", "").toIntOrNull() ?: 24
    }
}
