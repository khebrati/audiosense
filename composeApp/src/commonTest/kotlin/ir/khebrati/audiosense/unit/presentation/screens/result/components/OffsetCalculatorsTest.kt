package ir.khebrati.audiosense.unit.presentation.screens.result.components

import androidx.compose.ui.geometry.Size
import ir.khebrati.audiosense.presentation.screens.result.components.distributePointsUniformInRange
import kotlin.test.Test
import kotlin.test.assertEquals

class OffsetCalculatorsTest{
    @Test
    fun octavesXOffsets_givenSampleSize_returnsCorrectOffsets(){
        val size = Size(width = 100f, height = 100f)
//        val offsets = distributePointsUniformInRange(size, octavesSubset = listOf(0,1,2,3,4,5), pointsSubset = listOf(0,1,2,3,4,5,6,7,8,9,10))
//        assertEquals(listOf(0f,10f,20f,30f,40f,50f),offsets)
    }
}