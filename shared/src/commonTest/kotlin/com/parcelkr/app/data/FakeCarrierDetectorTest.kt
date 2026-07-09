package com.parcelkr.app.data

import com.parcelkr.app.data.fake.FakeCarrierDetector
import com.parcelkr.app.domain.model.Carrier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FakeCarrierDetectorTest {
    private val detector = FakeCarrierDetector()

    @Test fun long_number_detects_cj_confidently() {
        val g = detector.detect("657606146365")
        assertEquals(Carrier.CJ, g.carrier)
        assertTrue(g.confident)
    }
    @Test fun blank_is_unknown_and_unsure() {
        val g = detector.detect("   ")
        assertEquals(Carrier.UNKNOWN, g.carrier)
        assertFalse(g.confident)
    }
    @Test fun short_number_is_unsure() {
        assertFalse(detector.detect("123").confident)
    }
}
