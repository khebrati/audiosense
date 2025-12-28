package ir.khebrati.audiosense.unit.data.source.remote

import co.touchlab.kermit.Logger
import ir.khebrati.audiosense.data.source.local.entity.LocalTest
import ir.khebrati.audiosense.data.source.remote.AudiogramPosterImpl
import ir.khebrati.audiosense.data.source.remote.RemoteAuthHandler
import ir.khebrati.audiosense.data.source.remote.TokenManager
import ir.khebrati.audiosense.data.source.remote.entity.RemoteTest
import ir.khebrati.audiosense.data.toRemote
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AudiogramPosterTest {

    @OptIn(ExperimentalTime::class)
    @Test
    fun `Test posting audiogram to server`() {
        val tokenManager = TokenManager()
        val authHandler = RemoteAuthHandler(tokenManager)
        val poster = AudiogramPosterImpl(authHandler)

        // Create a sample LocalTest with test data
        val sampleTest = LocalTest(
            id = "test-id-12345",
            dateTime = Clock.System.now(),
            noiseDuringTest = 30,
            leftAC = mapOf(
                125 to 10,
                250 to 15,
                500 to 20,
                1000 to 25,
                2000 to 30,
                4000 to 40,
                8000 to 50
            ),
            rightAC = mapOf(
                125 to 10,
                250 to 10,
                500 to 15,
                1000 to 20,
                2000 to 25,
                4000 to 35,
                8000 to 45
            ),
            headphoneId = "64f1a2b3c4d5e6f7a8b9c0d1",
            personName = "Test User",
            personAge = 30,
            hasHearingAidExperience = false
        )

        // Log the body that will be sent
        val remoteTest = sampleTest.toRemote()
        val body = authHandler.json.encodeToString(RemoteTest.serializer(), remoteTest)
        Logger.d { "Request body being sent: $body" }

        runBlocking {
            val result = poster.postAudiogram(sampleTest)
            Logger.d { "Post audiogram result: $result" }
        }
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `Test posting audiogram directly and log full response`() {
        val tokenManager = TokenManager()
        val authHandler = RemoteAuthHandler(tokenManager)

        // Create sample RemoteTest directly
        val remoteTest = RemoteTest(
            age = 30,
            hearingAidExperience = true,
            date = Clock.System.now().toString(),
            leftAC = mapOf(
                "125" to 10,
                "250" to 15,
                "500" to 20,
                "1000" to 25,
                "2000" to 30,
                "4000" to 40,
                "8000" to 50
            ),
            rightAC = mapOf(
                "125" to 10,
                "250" to 10,
                "500" to 15,
                "1000" to 20,
                "2000" to 25,
                "4000" to 35,
                "8000" to 45
            ),
            headphoneModel = "64f1a2b3c4d5e6f7a8b9c0d1"
        )

        val body = authHandler.json.encodeToString(RemoteTest.serializer(), remoteTest)
        Logger.d { "Request body: $body" }
        println("Request body: $body")

        runBlocking {
            authHandler.authenticatedPost(
                endpoint = "/audiogram",
                body = body,
                onSuccess = { responseText ->
                    Logger.d { "SUCCESS Response: $responseText" }
                    println("SUCCESS Response: $responseText")
                    true
                },
                onInvalidCredentials = {
                    Logger.d { "INVALID CREDENTIALS" }
                    println("INVALID CREDENTIALS")
                    false
                },
                onError = { error ->
                    Logger.e { "ERROR Response: $error" }
                    println("ERROR Response: $error")
                    false
                }
            )
        }
    }
}

