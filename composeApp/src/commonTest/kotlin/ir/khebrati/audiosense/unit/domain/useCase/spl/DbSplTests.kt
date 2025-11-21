package ir.khebrati.audiosense.unit.domain.useCase.spl

import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.domain.useCase.spl.dbHl
import ir.khebrati.audiosense.domain.useCase.spl.findClosestInList
import kotlin.test.Test
import kotlin.test.assertEquals

class DbSplTests {
    @Test
    fun dbHl_givenAFloatValueNotInRange_mapsToCorrectDbHlValue(){
    }
    @Test
    fun findClosestInList_givenAFloat_findsItsNearest(){
        //arrange
        val test = 23.4
        val list = AcousticConstants.allPossibleDbHLs
        //action
        val dbHl = findClosestInList(test, list)
        assertEquals(25,dbHl)
    }
}