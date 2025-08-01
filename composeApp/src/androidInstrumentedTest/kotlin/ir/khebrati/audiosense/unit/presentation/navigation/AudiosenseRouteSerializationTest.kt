package ir.khebrati.audiosense.unit.presentation.navigation

import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertTrue

class AudiosenseRouteSerializationTest {
    @Test
    fun testSerializableRoutes_whenSerialized_throwNoExceptions(){
        val routes = listOf(
            AudiosenseRoute.NoiseMeter,
            AudiosenseRoute.Setting,
            AudiosenseRoute.DescriptiveResult,
            AudiosenseRoute.Test,
            AudiosenseRoute.Results
        )
        routes.forEach { route ->
            // This will throw an exception if the route is not serializable
            val serialized = Json.encodeToString(AudiosenseRoute.serializer(), route)
            val deserialized = Json.decodeFromString(AudiosenseRoute.serializer(), serialized)
            assertTrue(message = "Route serialization failed for: $route") {
                route == deserialized
            }
        }
    }
}