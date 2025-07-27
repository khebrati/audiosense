package ir.khebrati.audiosense.unit.data.source

import ir.khebrati.audiosense.data.source.local.Convertors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ConvertorsTest {
    @Test
    fun stringToMap_givenSampleEncodedMap_extractsIt() {
        //Arrange
        val convertor = createConvertors()
        val encodedMap = "1:2,3:4,5:6"
        val expectedMap = mapOf(1 to 2, 3 to 4, 5 to 6)
        //Action
        val result = convertor.stringToMap(encodedMap)
        //Assert
        assertTrue(result == expectedMap, message = "Expected map $expectedMap but got $result")
    }

    @Test
    fun stringToMapAndMapToString_whenChained_canInterpretEachOtherResults() {
        //Arrange
        val convertor = createConvertors()
        val sampleString = "1:2,3:4,5:6"
        //Action
        val encoded = convertor.stringToMap(sampleString)
        val decoded = convertor.mapToString(encoded)
        //Assert
        assertEquals(sampleString, decoded)
    }

    @Test
    fun stringToMap_givenEmptyString_returnsEmptyMap() {
        //Arrange
        val convertor = createConvertors()
        val emptyString = ""
        //Action
        val result = convertor.stringToMap(emptyString)
        //Assert
        assertTrue { result.isEmpty() }
    }

    @Test
    fun mapToString_givenEmptyMap_returnsEmptyString() {
        //Arrange
        val convertor = createConvertors()
        val emptyMap = emptyMap<Int, Int>()
        //Action
        val result = convertor.mapToString(emptyMap)
        //Assert
        assertTrue { result.isEmpty() }
    }

    private fun createConvertors() = Convertors()
}