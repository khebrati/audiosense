package ir.khebrati.audiosense.unit.data.source

import ir.khebrati.audiosense.data.source.local.Convertors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ConvertorsTest {
    @Test
    fun stringToMap_givenSampleEncodedMap_Pair_extractsIt() {
        //Arrange
        val convertor = createConvertors()
        val encodedMap = "1:2,3:4,5:6"
        val expectedMap = mapOf(1 to 2, 3 to 4, 5 to 6)
        //Action
        val result = convertor.stringToMapPair(encodedMap)
        //Assert
        assertTrue(result == expectedMap, message = "Expected map $expectedMap but got $result")
    }

    @Test
    fun stringToMapAndMapPairToString_whenChained_canInterpretEachOtherResultsPair() {
        //Arrange
        val convertor = createConvertors()
        val sampleString = "1:2,3:4,5:6"
        //Action
        val encoded = convertor.stringToMapPair(sampleString)
        val decoded = convertor.mapPairToString(encoded)
        //Assert
        assertEquals(sampleString, decoded)
    }

    @Test
    fun stringToMap_givenEmptyString_returnsEmptyMapPair() {
        //Arrange
        val convertor = createConvertors()
        val emptyString = ""
        //Action
        val result = convertor.stringToMapPair(emptyString)
        //Assert
        assertTrue { result.isEmpty() }
    }

    @Test
    fun mapToString_givenEmptyMap_Pair_returnsEmptyString() {
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