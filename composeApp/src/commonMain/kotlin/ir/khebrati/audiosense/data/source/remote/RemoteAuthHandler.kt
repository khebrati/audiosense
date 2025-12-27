package ir.khebrati.audiosense.data.source.remote

import AudioSense.composeApp.BuildConfig
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val BASE_URL = "http://188.121.121.80:3000/api"

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val expiresIn: String? = null,
    val error: String? = null,
)

class RemoteAuthHandler(private val tokenManager: TokenManager) {

    val client = HttpClient(CIO)
    val json = Json { ignoreUnknownKeys = true }

    suspend fun getValidToken(): String? {
        tokenManager.getToken()?.let {
            return it
        }
        return login()
    }

    suspend fun login(): String {
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
            val hours = parseExpiresIn(loginResponse.expiresIn)
            tokenManager.setToken(loginResponse.token, hours)
            return loginResponse.token
        } else {
            throw Exception("Login failed: ${loginResponse.error}")
        }
    }

    suspend fun <T> authenticatedGet(
        endpoint: String,
        onSuccess: suspend (String) -> T,
        onInvalidCredentials: suspend () -> T,
        onError: suspend (String?) -> T,
    ): T {
        val token = getValidToken() ?: return onError("Failed to get token")
        return executeAuthenticatedGet(endpoint, token, onSuccess, onInvalidCredentials, onError)
    }

    private suspend fun <T> executeAuthenticatedGet(
        endpoint: String,
        token: String,
        onSuccess: suspend (String) -> T,
        onInvalidCredentials: suspend () -> T,
        onError: suspend (String?) -> T,
    ): T {
        val response: HttpResponse =
            client.get("$BASE_URL$endpoint") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

        val responseText = response.bodyAsText()
        Logger.d { "GET $endpoint response: $responseText" }

        if (!response.status.isSuccess()) {
            val errorResponse = json.decodeFromString<LoginResponse>(responseText)
            if (errorResponse.error == "Invalid credentials") {
                tokenManager.clearToken()
                val newToken = login()
                return executeAuthenticatedGet(endpoint, newToken, onSuccess, onInvalidCredentials, onError)
            }
            return onError(errorResponse.error)
        }

        return onSuccess(responseText)
    }

    suspend fun <T> authenticatedPost(
        endpoint: String,
        body: String,
        onSuccess: suspend (String) -> T,
        onInvalidCredentials: suspend () -> T,
        onError: suspend (String?) -> T,
    ): T {
        val token = getValidToken() ?: return onError("Failed to get token")
        return executeAuthenticatedPost(endpoint, body, token, onSuccess, onInvalidCredentials, onError)
    }

    private suspend fun <T> executeAuthenticatedPost(
        endpoint: String,
        body: String,
        token: String,
        onSuccess: suspend (String) -> T,
        onInvalidCredentials: suspend () -> T,
        onError: suspend (String?) -> T,
    ): T {
        val response: HttpResponse =
            client.post("$BASE_URL$endpoint") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(body)
            }

        val responseText = response.bodyAsText()
        Logger.d { "POST $endpoint response: $responseText" }

        if (!response.status.isSuccess()) {
            val errorResponse = json.decodeFromString<LoginResponse>(responseText)
            if (errorResponse.error == "Invalid credentials") {
                tokenManager.clearToken()
                val newToken = login()
                return executeAuthenticatedPost(endpoint, body, newToken, onSuccess, onInvalidCredentials, onError)
            }
            return onError(errorResponse.error)
        }

        return onSuccess(responseText)
    }

    private fun parseExpiresIn(expiresIn: String?): Int {
        if (expiresIn == null) return 24
        return expiresIn.replace("h", "").toIntOrNull() ?: 24
    }
}

