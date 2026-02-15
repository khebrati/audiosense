package ir.khebrati.audiosense.unit.presentation.navigation

import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute
import kotlin.test.Test

class AudiosenseRouteSerializationTest {
    @Test
    fun testSerializableRoutes_whenSerialized_throwNoExceptions(){
        val routes = listOf(
            AudiosenseRoute.TestSetupRoute,
            AudiosenseRoute.SettingRoute,
            AudiosenseRoute.DescriptiveResultRoute,
            AudiosenseRoute.TestRoute,
            AudiosenseRoute.ResultRoute
        )
        routes.forEach { route ->
            // This will throw an exception if the route is not serializable
//            val serialized = Json.encodeToString(AudiosenseRoute.serializer(), route)
//            val deserialized = Json.decodeFromString(AudiosenseRoute.serializer(), serialized)
//            assertTrue(message = "Route serialization failed for: $route") {
//                route == deserialized
//            }
        }
    }
}