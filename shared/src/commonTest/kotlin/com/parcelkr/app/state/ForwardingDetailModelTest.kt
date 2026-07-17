package com.parcelkr.app.state

import com.parcelkr.app.data.fake.FakeOverseasTrackingApi
import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.domain.OverseasTrackingApi
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.OverseasTrackingResult
import com.parcelkr.app.domain.model.TrackingResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private class BoomOverseasApiForDetail : OverseasTrackingApi {
    override suspend fun register(trackingNumber: String) = Unit
    override suspend fun track(trackingNumber: String): OverseasTrackingResult = throw RuntimeException("boom")
}

private class BoomTrackingApiForDetail : TrackingApi {
    override suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult = throw RuntimeException("boom")
}

class ForwardingDetailModelTest {
    @Test fun load_overseas_returns_result_and_clears_failed_flag() = runTest {
        val m = ForwardingDetailModel(FakeOverseasTrackingApi(), FakeTrackingApi())

        val result = m.loadOverseas("RB1")

        assertEquals("RB1", result?.trackingNumber)
        assertFalse(m.overseasLoadFailed.value)
    }

    @Test fun load_overseas_sets_failed_flag_on_exception() = runTest {
        val m = ForwardingDetailModel(BoomOverseasApiForDetail(), FakeTrackingApi())

        val result = m.loadOverseas("RB1")

        assertNull(result)
        assertTrue(m.overseasLoadFailed.value)
    }

    @Test fun load_domestic_returns_result_and_clears_failed_flag() = runTest {
        val m = ForwardingDetailModel(FakeOverseasTrackingApi(), FakeTrackingApi())

        val result = m.loadDomestic("657606146365", Carrier.CJ)

        assertEquals("657606146365", result?.trackingNumber)
        assertFalse(m.domesticLoadFailed.value)
    }

    @Test fun load_domestic_sets_failed_flag_on_exception() = runTest {
        val m = ForwardingDetailModel(FakeOverseasTrackingApi(), BoomTrackingApiForDetail())

        val result = m.loadDomestic("657606146365", Carrier.CJ)

        assertNull(result)
        assertTrue(m.domesticLoadFailed.value)
    }
}
