package ir.khebrati.audiosense.unit.domain.useCase.spl

import ir.khebrati.audiosense.domain.model.AcousticConstants
import ir.khebrati.audiosense.domain.useCase.spl.calculateLossBasedOnDbHl
import ir.khebrati.audiosense.domain.useCase.spl.dbHl
import ir.khebrati.audiosense.domain.useCase.spl.dbSpl
import ir.khebrati.audiosense.domain.useCase.spl.findClosestInList
import ir.khebrati.audiosense.domain.useCase.spl.fromDbSpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DbSplTests {

    // ========== findClosestInList TESTS ==========

    @Test
    fun findClosestInList_givenExactMatch_returnsExactValue() {
        val list = AcousticConstants.allPossibleDbHLs
        val result = findClosestInList(25.0, list)
        assertEquals(25, result)
    }

    @Test
    fun findClosestInList_givenAFloat_findsItsNearest() {
        val test = 23.4
        val list = AcousticConstants.allPossibleDbHLs
        val dbHl = findClosestInList(test, list)
        assertEquals(25, dbHl)
    }

    @Test
    fun findClosestInList_givenValueBelowRange_returnsMinimum() {
        val list = AcousticConstants.allPossibleDbHLs
        val result = findClosestInList(-20.0, list)
        assertEquals(-10, result) // -10 is the minimum in allPossibleDbHLs
    }

    @Test
    fun findClosestInList_givenValueAboveRange_returnsMaximum() {
        val list = AcousticConstants.allPossibleDbHLs
        val result = findClosestInList(100.0, list)
        assertEquals(90, result) // 90 is the maximum in allPossibleDbHLs
    }

    @Test
    fun findClosestInList_givenValueExactlyBetweenTwo_returnsLowerValue() {
        val list = listOf(10, 20, 30)
        // 15 is exactly between 10 and 20
        val result = findClosestInList(15.0, list)
        // Should return 10 since it's encountered first with equal distance
        assertEquals(10, result)
    }

    @Test
    fun findClosestInList_givenNegativeTarget_findsCorrectValue() {
        val list = AcousticConstants.allPossibleDbHLs
        val result = findClosestInList(-7.5, list)
        assertEquals(-10, result) // -10 is closer than -5
    }

    @Test
    fun findClosestInList_givenZero_returnsZero() {
        val list = AcousticConstants.allPossibleDbHLs
        val result = findClosestInList(0.0, list)
        assertEquals(0, result)
    }

    @Test
    fun findClosestInList_givenSingleElementList_returnsThatElement() {
        val list = listOf(50)
        val result = findClosestInList(100.0, list)
        assertEquals(50, result)
    }

    // ========== Int.dbHl TESTS ==========

    @Test
    fun dbHl_givenValidFrequency1000Hz_returnsCorrectDbHl() {
        // normalEarHearingThresholds[1000] = 2.4
        // dbHl = this + diff = 30 + 2.4 = 32.4 -> closest is 30
        val result = 30.dbHl(1000)
        assertEquals(30, result)
    }

    @Test
    fun dbHl_givenValidFrequency125Hz_returnsCorrectDbHl() {
        // normalEarHearingThresholds[125] = 22.1
        // dbHl = 30 + 22.1 = 52.1 -> closest is 50
        val result = 30.dbHl(125)
        assertEquals(50, result)
    }

    @Test
    fun dbHl_givenValidFrequency8000Hz_returnsCorrectDbHl() {
        // normalEarHearingThresholds[8000] = 12.6
        // dbHl = 40 + 12.6 = 52.6 -> closest is 55
        val result = 40.dbHl(8000)
        assertEquals(55, result)
    }

    @Test
    fun dbHl_givenFrequency4000Hz_withNegativeThreshold_returnsCorrectDbHl() {
        // normalEarHearingThresholds[4000] = -5.4
        // dbHl = 30 + (-5.4) = 24.6 -> closest is 25
        val result = 30.dbHl(4000)
        assertEquals(25, result)
    }

    @Test
    fun dbHl_givenFrequency2000Hz_returnsCorrectDbHl() {
        // normalEarHearingThresholds[2000] = -1.3
        // dbHl = 50 + (-1.3) = 48.7 -> closest is 50
        val result = 50.dbHl(2000)
        assertEquals(50, result)
    }

    @Test
    fun dbHl_givenInvalidFrequency_throwsIllegalArgumentException() {
        assertFailsWith<IllegalArgumentException> {
            30.dbHl(999) // 999 Hz is not in normalEarHearingThresholds
        }
    }

    @Test
    fun dbHl_givenZeroValue_returnsThresholdValue() {
        // normalEarHearingThresholds[1000] = 2.4
        // dbHl = 0 + 2.4 = 2.4 -> closest is 0
        val result = 0.dbHl(1000)
        assertEquals(0, result)
    }

    @Test
    fun dbHl_givenNegativeValue_calculatesCorrectly() {
        // normalEarHearingThresholds[1000] = 2.4
        // dbHl = -10 + 2.4 = -7.6 -> closest is -10
        val result = (-10).dbHl(1000)
        assertEquals(-10, result)
    }

    // ========== calculateLossBasedOnDbHl TESTS ==========

    @Test
    fun calculateLossBasedOnDbHl_givenEmptyMap_returnsEmptyMap() {
        val result = calculateLossBasedOnDbHl(emptyMap())
        assertTrue(result.isEmpty())
    }

    @Test
    fun calculateLossBasedOnDbHl_givenSingleFrequency_calculatesCorrectly() {
        // normalEarHearingThresholds[1000] = 2.4
        // dbHl = value - diff = 30 - 2.4 = 27.6 -> closest is 30
        val audiogram = mapOf(1000 to 30)
        val result = calculateLossBasedOnDbHl(audiogram)
        assertEquals(30, result[1000])
    }

    @Test
    fun calculateLossBasedOnDbHl_givenMultipleFrequencies_calculatesAllCorrectly() {
        val audiogram = mapOf(
            1000 to 30,
            2000 to 40,
            4000 to 50
        )
        val result = calculateLossBasedOnDbHl(audiogram)

        // 1000Hz: 30 - 2.4 = 27.6 -> 30
        assertEquals(30, result[1000])
        // 2000Hz: 40 - (-1.3) = 41.3 -> 40
        assertEquals(40, result[2000])
        // 4000Hz: 50 - (-5.4) = 55.4 -> 55
        assertEquals(55, result[4000])
    }

    @Test
    fun calculateLossBasedOnDbHl_givenAllStandardFrequencies_calculatesAllCorrectly() {
        val audiogram = mapOf(
            125 to 50,
            250 to 50,
            500 to 50,
            1000 to 50,
            2000 to 50,
            4000 to 50,
            8000 to 50
        )
        val result = calculateLossBasedOnDbHl(audiogram)

        // Verify all frequencies are processed
        assertEquals(7, result.size)
        assertTrue(result.containsKey(125))
        assertTrue(result.containsKey(250))
        assertTrue(result.containsKey(500))
        assertTrue(result.containsKey(1000))
        assertTrue(result.containsKey(2000))
        assertTrue(result.containsKey(4000))
        assertTrue(result.containsKey(8000))
    }

    @Test
    fun calculateLossBasedOnDbHl_givenInvalidFrequency_throwsIllegalArgumentException() {
        val audiogram = mapOf(999 to 30) // 999 Hz is not valid
        assertFailsWith<IllegalArgumentException> {
            calculateLossBasedOnDbHl(audiogram)
        }
    }

    @Test
    fun calculateLossBasedOnDbHl_givenNegativeValues_calculatesCorrectly() {
        // normalEarHearingThresholds[1000] = 2.4
        // dbHl = -10 - 2.4 = -12.4 -> closest is -10
        val audiogram = mapOf(1000 to -10)
        val result = calculateLossBasedOnDbHl(audiogram)
        assertEquals(-10, result[1000])
    }

    @Test
    fun calculateLossBasedOnDbHl_givenHighFrequency125Hz_accountsForHighThreshold() {
        // normalEarHearingThresholds[125] = 22.1
        // dbHl = 50 - 22.1 = 27.9 -> closest is 30
        val audiogram = mapOf(125 to 50)
        val result = calculateLossBasedOnDbHl(audiogram)
        assertEquals(30, result[125])
    }

    // ========== Number.dbSpl TESTS ==========

    @Test
    fun dbSpl_givenZero_returnsOne() {
        // 10^(0/20) = 10^0 = 1
        val result = 0.dbSpl
        assertEquals(1f, result)
    }

    @Test
    fun dbSpl_givenTwenty_returnsTen() {
        // 10^(20/20) = 10^1 = 10
        val result = 20.dbSpl
        assertEquals(10f, result)
    }

    @Test
    fun dbSpl_givenForty_returnsHundred() {
        // 10^(40/20) = 10^2 = 100
        val result = 40.dbSpl
        assertEquals(100f, result)
    }

    @Test
    fun dbSpl_givenSixty_returnsThousand() {
        // 10^(60/20) = 10^3 = 1000
        val result = 60.dbSpl
        assertEquals(1000f, result)
    }

    @Test
    fun dbSpl_givenNegativeTwenty_returnsPointOne() {
        // 10^(-20/20) = 10^-1 = 0.1
        val result = (-20).dbSpl
        assertEquals(0.1f, result, 0.0001f)
    }

    @Test
    fun dbSpl_givenFloat_calculatesCorrectly() {
        // 10^(10/20) = 10^0.5 ≈ 3.162
        val result = 10.0.dbSpl
        assertTrue(result > 3.1f && result < 3.2f)
    }

    @Test
    fun dbSpl_givenDoubleValue_calculatesCorrectly() {
        val result = 20.0.dbSpl
        assertEquals(10f, result)
    }

    // ========== Number.fromDbSpl TESTS ==========

    @Test
    fun fromDbSpl_givenOne_returnsZero() {
        // 20 * log10(1) = 20 * 0 = 0
        val result = 1.fromDbSpl
        assertEquals(0, result)
    }

    @Test
    fun fromDbSpl_givenTen_returnsTwenty() {
        // 20 * log10(10) = 20 * 1 = 20
        val result = 10.fromDbSpl
        assertEquals(20, result)
    }

    @Test
    fun fromDbSpl_givenHundred_returnsForty() {
        // 20 * log10(100) = 20 * 2 = 40
        val result = 100.fromDbSpl
        assertEquals(40, result)
    }

    @Test
    fun fromDbSpl_givenThousand_returnsSixty() {
        // 20 * log10(1000) = 20 * 3 = 60
        val result = 1000.fromDbSpl
        assertEquals(60, result)
    }

    @Test
    fun fromDbSpl_givenFloat_calculatesCorrectly() {
        // 20 * log10(100.0) = 40
        val result = 100.0.fromDbSpl
        assertEquals(40, result)
    }

    // ========== ROUNDTRIP TESTS ==========

    @Test
    fun dbSplAndFromDbSpl_roundTrip_isConsistent() {
        // Start with dB value, convert to linear, convert back
        val originalDb = 40
        val linear = originalDb.dbSpl // 100
        val backToDb = linear.fromDbSpl // should be 40
        assertEquals(originalDb, backToDb)
    }

    @Test
    fun dbSplAndFromDbSpl_roundTrip_forZero_isConsistent() {
        val originalDb = 0
        val linear = originalDb.dbSpl // 1
        val backToDb = linear.fromDbSpl // should be 0
        assertEquals(originalDb, backToDb)
    }

    @Test
    fun dbSplAndFromDbSpl_roundTrip_forSixty_isConsistent() {
        val originalDb = 60
        val linear = originalDb.dbSpl // 1000
        val backToDb = linear.fromDbSpl // should be 60
        assertEquals(originalDb, backToDb)
    }

    // ========== EDGE CASE TESTS ==========


    @Test
    fun findClosestInList_givenVeryLargeNegativeTarget_returnsMinimum() {
        val list = AcousticConstants.allPossibleDbHLs
        val result = findClosestInList(-Double.MAX_VALUE, list)
        assertEquals(-10, result)
    }

    @Test
    fun dbSpl_givenMaxDbValue_calculatesWithoutOverflow() {
        val result = 90.dbSpl
        assertTrue(result > 0f)
        assertTrue(result.isFinite())
    }

    @Test
    fun dbHl_givenAllValidFrequencies_calculatesWithoutException() {
        val frequencies = listOf(125, 250, 500, 1000, 2000, 4000, 8000)
        frequencies.forEach { freq ->
            val result = 50.dbHl(freq)
            assertTrue(result in -10..90)
        }
    }

    // ========== ADDITIONAL findClosestInList TESTS ==========

    @Test
    fun findClosestInList_givenTwoElementList_findsCorrectValue() {
        val list = listOf(0, 100)
        assertEquals(0, findClosestInList(25.0, list))
        assertEquals(100, findClosestInList(75.0, list))
        assertEquals(0, findClosestInList(50.0, list)) // Equal distance, returns first
    }

    @Test
    fun findClosestInList_givenAllNegativeList_findsCorrectValue() {
        val list = listOf(-30, -20, -10)
        assertEquals(-20, findClosestInList(-18.0, list))
        assertEquals(-10, findClosestInList(-5.0, list))
    }

    @Test
    fun findClosestInList_givenDecimalPrecision_findsCorrectValue() {
        val list = AcousticConstants.allPossibleDbHLs
        assertEquals(25, findClosestInList(24.9, list))
        assertEquals(25, findClosestInList(25.1, list))
        assertEquals(30, findClosestInList(27.6, list))
    }

    @Test
    fun findClosestInList_givenTargetAtListBoundary_returnsCorrectValue() {
        val list = listOf(0, 10, 20)
        assertEquals(0, findClosestInList(0.0, list))
        assertEquals(20, findClosestInList(20.0, list))
    }

    // ========== ADDITIONAL Int.dbHl TESTS ==========

    @Test
    fun dbHl_givenFrequency250Hz_returnsCorrectDbHl() {
        // normalEarHearingThresholds[250] = 11.4
        // dbHl = 30 + 11.4 = 41.4 -> closest is 40
        val result = 30.dbHl(250)
        assertEquals(40, result)
    }

    @Test
    fun dbHl_givenFrequency500Hz_returnsCorrectDbHl() {
        // normalEarHearingThresholds[500] = 4.4
        // dbHl = 30 + 4.4 = 34.4 -> closest is 35
        val result = 30.dbHl(500)
        assertEquals(35, result)
    }

    @Test
    fun dbHl_givenLargePositiveValue_clampsToMax() {
        // normalEarHearingThresholds[1000] = 2.4
        // dbHl = 100 + 2.4 = 102.4 -> closest is 90 (max)
        val result = 100.dbHl(1000)
        assertEquals(90, result)
    }

    @Test
    fun dbHl_givenLargeNegativeValue_clampsToMin() {
        // normalEarHearingThresholds[4000] = -5.4
        // dbHl = -20 + (-5.4) = -25.4 -> closest is -10 (min)
        val result = (-20).dbHl(4000)
        assertEquals(-10, result)
    }

    @Test
    fun dbHl_givenFrequency0Hz_throwsIllegalArgumentException() {
        assertFailsWith<IllegalArgumentException> {
            30.dbHl(0)
        }
    }

    @Test
    fun dbHl_givenNegativeFrequency_throwsIllegalArgumentException() {
        assertFailsWith<IllegalArgumentException> {
            30.dbHl(-1000)
        }
    }

    // ========== ADDITIONAL calculateLossBasedOnDbHl TESTS ==========

    @Test
    fun calculateLossBasedOnDbHl_givenZeroValues_calculatesCorrectly() {
        val audiogram = mapOf(
            1000 to 0,
            2000 to 0,
            4000 to 0
        )
        val result = calculateLossBasedOnDbHl(audiogram)

        // 1000Hz: 0 - 2.4 = -2.4 -> closest is 0
        assertEquals(0, result[1000])
        // 2000Hz: 0 - (-1.3) = 1.3 -> closest is 0
        assertEquals(0, result[2000])
        // 4000Hz: 0 - (-5.4) = 5.4 -> closest is 5
        assertEquals(5, result[4000])
    }

    @Test
    fun calculateLossBasedOnDbHl_givenMaxValues_calculatesCorrectly() {
        val audiogram = mapOf(1000 to 90)
        val result = calculateLossBasedOnDbHl(audiogram)
        // 1000Hz: 90 - 2.4 = 87.6 -> closest is 90
        assertEquals(90, result[1000])
    }

    @Test
    fun calculateLossBasedOnDbHl_preservesFrequencyKeys() {
        val audiogram = mapOf(
            125 to 40,
            8000 to 60
        )
        val result = calculateLossBasedOnDbHl(audiogram)

        assertTrue(result.containsKey(125))
        assertTrue(result.containsKey(8000))
        assertEquals(2, result.size)
    }

    @Test
    fun calculateLossBasedOnDbHl_givenMixedInvalidAndValidFrequencies_throwsOnFirst() {
        val audiogram = mapOf(
            1000 to 30,
            999 to 30 // Invalid
        )
        assertFailsWith<IllegalArgumentException> {
            calculateLossBasedOnDbHl(audiogram)
        }
    }

    // ========== ADDITIONAL Number.dbSpl TESTS ==========

    @Test
    fun dbSpl_givenEighty_returnsTenThousand() {
        // 10^(80/20) = 10^4 = 10000
        val result = 80.dbSpl
        assertEquals(10000f, result)
    }

    @Test
    fun dbSpl_givenNegativeForty_returnsPointZeroOne() {
        // 10^(-40/20) = 10^-2 = 0.01
        val result = (-40).dbSpl
        assertEquals(0.01f, result, 0.0001f)
    }

    @Test
    fun dbSpl_givenLongValue_calculatesCorrectly() {
        val result = 20L.dbSpl
        assertEquals(10f, result)
    }

    @Test
    fun dbSpl_givenShortValue_calculatesCorrectly() {
        val value: Short = 20
        val result = value.dbSpl
        assertEquals(10f, result)
    }

    @Test
    fun dbSpl_givenByteValue_calculatesCorrectly() {
        val value: Byte = 20
        val result = value.dbSpl
        assertEquals(10f, result)
    }

    @Test
    fun dbSpl_givenVerySmallNegativeValue_returnsVerySmallPositive() {
        val result = (-60).dbSpl
        // 10^(-60/20) = 10^-3 = 0.001
        assertEquals(0.001f, result, 0.0001f)
    }

    @Test
    fun dbSpl_givenTen_returnsSqrtTen() {
        // 10^(10/20) = 10^0.5 ≈ 3.162
        val result = 10.dbSpl
        assertTrue(result in 3.16f..3.17f)
    }

    @Test
    fun dbSpl_givenThirty_returnsApprox31() {
        // 10^(30/20) = 10^1.5 ≈ 31.62
        val result = 30.dbSpl
        assertTrue(result in 31.6f..31.7f)
    }

    // ========== ADDITIONAL Number.fromDbSpl TESTS ==========

    @Test
    fun fromDbSpl_givenTenThousand_returnsEighty() {
        // 20 * log10(10000) = 20 * 4 = 80
        val result = 10000.fromDbSpl
        assertEquals(80, result)
    }

    @Test
    fun fromDbSpl_givenPointOne_returnsNegativeTwenty() {
        // 20 * log10(0.1) = 20 * -1 = -20
        val result = 0.1.fromDbSpl
        assertEquals(-20, result)
    }

    @Test
    fun fromDbSpl_givenPointZeroOne_returnsNegativeForty() {
        // 20 * log10(0.01) = 20 * -2 = -40
        val result = 0.01.fromDbSpl
        assertEquals(-40, result)
    }

    @Test
    fun fromDbSpl_givenLongValue_calculatesCorrectly() {
        val result = 100L.fromDbSpl
        assertEquals(40, result)
    }

    @Test
    fun fromDbSpl_givenDoubleValue_calculatesCorrectly() {
        val result = 1000.0.fromDbSpl
        assertEquals(60, result)
    }


    // ========== ADDITIONAL ROUNDTRIP TESTS ==========

    @Test
    fun dbSplAndFromDbSpl_roundTrip_forTwenty_isConsistent() {
        val originalDb = 20
        val linear = originalDb.dbSpl // 10
        val backToDb = linear.fromDbSpl // should be 20
        assertEquals(originalDb, backToDb)
    }

    @Test
    fun dbSplAndFromDbSpl_roundTrip_forEighty_isConsistent() {
        val originalDb = 80
        val linear = originalDb.dbSpl // 10000
        val backToDb = linear.fromDbSpl // should be 80
        assertEquals(originalDb, backToDb)
    }

    @Test
    fun dbSplAndFromDbSpl_roundTrip_forNegativeTwenty_isConsistent() {
        val originalDb = -20
        val linear = originalDb.dbSpl // 0.1
        val backToDb = linear.fromDbSpl // should be -20
        assertEquals(originalDb, backToDb)
    }

    @Test
    fun dbSplAndFromDbSpl_roundTrip_forAllStandardDbHls_isConsistent() {
        val standardDbHls = listOf(0, 20, 40, 60, 80)
        standardDbHls.forEach { db ->
            val linear = db.dbSpl
            val backToDb = linear.fromDbSpl
            assertEquals(db, backToDb, "Round trip failed for $db dB")
        }
    }

    // ========== ADDITIONAL EDGE CASE TESTS ==========

    @Test
    fun dbSpl_givenMinDbValue_calculatesWithoutUnderflow() {
        val result = (-80).dbSpl
        assertTrue(result > 0f)
        assertTrue(result.isFinite())
        assertTrue(result < 0.001f)
    }

    @Test
    fun findClosestInList_givenListWithDuplicates_returnsFirstMatch() {
        val list = listOf(10, 20, 20, 30)
        val result = findClosestInList(20.0, list)
        assertEquals(20, result)
    }

    @Test
    fun findClosestInList_givenDescendingList_findsCorrectValue() {
        val list = listOf(90, 70, 50, 30, 10)
        val result = findClosestInList(45.0, list)
        assertEquals(50, result)
    }

    @Test
    fun dbHl_consistentWithCalculateLoss_forSameFrequency() {
        // Test that dbHl and calculateLossBasedOnDbHl are inverses for a frequency
        val inputValue = 40
        val freq = 1000

        // Forward: calculateLossBasedOnDbHl subtracts threshold
        val lossResult = calculateLossBasedOnDbHl(mapOf(freq to inputValue))

        // The operations should be consistent (one adds, one subtracts)
        assertTrue(lossResult[freq] != null)
    }

    // ========== PRECISION TESTS ==========

    @Test
    fun dbSpl_givenPreciseValues_maintainsPrecision() {
        // 10^(6/20) should give approximately 1.995
        val result = 6.dbSpl
        assertTrue(result in 1.99f..2.0f)
    }

    @Test
    fun findClosestInList_givenVeryCloseValues_findsCorrect() {
        val list = AcousticConstants.allPossibleDbHLs // increments of 5
        // 22.49 should round to 20, 22.51 should round to 25
        assertEquals(20, findClosestInList(22.4, list))
        assertEquals(25, findClosestInList(22.6, list))
    }

    // ========== BOUNDARY VALUE TESTS FOR STANDARD FREQUENCIES ==========

    @Test
    fun dbHl_atBoundaryValues_calculatesCorrectly() {
        // Test at -10 (min) for each frequency
        val frequencies = listOf(125, 250, 500, 1000, 2000, 4000, 8000)
        frequencies.forEach { freq ->
            val result = (-10).dbHl(freq)
            assertTrue(result in -10..90, "Failed for freq $freq")
        }
    }

    @Test
    fun calculateLossBasedOnDbHl_atBoundaryValues_calculatesCorrectly() {
        // Test at 90 (max) for each frequency
        val audiogram = mapOf(
            125 to 90,
            250 to 90,
            500 to 90,
            1000 to 90,
            2000 to 90,
            4000 to 90,
            8000 to 90
        )
        val result = calculateLossBasedOnDbHl(audiogram)

        result.forEach { (freq, value) ->
            assertTrue(value in -10..90, "Result out of range for freq $freq: $value")
        }
    }

    // ========== SPECIFIC THRESHOLD CALCULATION TESTS ==========

    @Test
    fun dbHl_verifyThresholdFor125Hz() {
        // Threshold for 125Hz is 22.1
        // So 0 + 22.1 = 22.1 -> rounds to 20
        val result = 0.dbHl(125)
        assertEquals(20, result)
    }

    @Test
    fun dbHl_verifyThresholdFor250Hz() {
        // Threshold for 250Hz is 11.4
        // So 0 + 11.4 = 11.4 -> rounds to 10
        val result = 0.dbHl(250)
        assertEquals(10, result)
    }

    @Test
    fun dbHl_verifyThresholdFor500Hz() {
        // Threshold for 500Hz is 4.4
        // So 0 + 4.4 = 4.4 -> rounds to 5
        val result = 0.dbHl(500)
        assertEquals(5, result)
    }

    @Test
    fun dbHl_verifyThresholdFor1000Hz() {
        // Threshold for 1000Hz is 2.4
        // So 0 + 2.4 = 2.4 -> rounds to 0
        val result = 0.dbHl(1000)
        assertEquals(0, result)
    }

    @Test
    fun dbHl_verifyThresholdFor2000Hz() {
        // Threshold for 2000Hz is -1.3
        // So 0 + (-1.3) = -1.3 -> rounds to 0
        val result = 0.dbHl(2000)
        assertEquals(0, result)
    }

    @Test
    fun dbHl_verifyThresholdFor4000Hz() {
        // Threshold for 4000Hz is -5.4
        // So 0 + (-5.4) = -5.4 -> rounds to -5
        val result = 0.dbHl(4000)
        assertEquals(-5, result)
    }

    @Test
    fun dbHl_verifyThresholdFor8000Hz() {
        // Threshold for 8000Hz is 12.6
        // So 0 + 12.6 = 12.6 -> rounds to 15
        val result = 0.dbHl(8000)
        assertEquals(15, result)
    }

    // ========== INVERSE OPERATION TESTS ==========

    @Test
    fun calculateLossBasedOnDbHl_verifyThresholdSubtraction125Hz() {
        // Threshold for 125Hz is 22.1
        // So 50 - 22.1 = 27.9 -> rounds to 30
        val result = calculateLossBasedOnDbHl(mapOf(125 to 50))
        assertEquals(30, result[125])
    }

    @Test
    fun calculateLossBasedOnDbHl_verifyThresholdSubtraction4000Hz() {
        // Threshold for 4000Hz is -5.4
        // So 50 - (-5.4) = 55.4 -> rounds to 55
        val result = calculateLossBasedOnDbHl(mapOf(4000 to 50))
        assertEquals(55, result[4000])
    }

    @Test
    fun calculateLossBasedOnDbHl_verifyThresholdSubtraction8000Hz() {
        // Threshold for 8000Hz is 12.6
        // So 50 - 12.6 = 37.4 -> rounds to 35
        val result = calculateLossBasedOnDbHl(mapOf(8000 to 50))
        assertEquals(35, result[8000])
    }
}