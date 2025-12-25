package ir.khebrati.audiosense.data.source.remote

class TokenManager {
    private var token: String? = null
    private var tokenExpiresAtMillis: Long? = null

    fun getToken(): String? {
        val expiresAt = tokenExpiresAtMillis ?: return null
        val currentToken = token ?: return null

        // Check if token is still valid
        if (kotlin.time.Clock.System.now().toEpochMilliseconds() < expiresAt) {
            return currentToken
        }

        // Token expired, clear it
        clearToken()
        return null
    }

    fun setToken(newToken: String, expiresInHours: Int = 24) {
        token = newToken
        tokenExpiresAtMillis = kotlin.time.Clock.System.now().toEpochMilliseconds() + (expiresInHours * 60 * 60 * 1000L)
    }

    fun clearToken() {
        token = null
        tokenExpiresAtMillis = null
    }
}

