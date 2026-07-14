package com.parcelkr.app.state

import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.TrackingResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

private class BoomDetailTrackingApi : TrackingApi {
    override suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult =
        throw RuntimeException("boom")
}

class DetailModelTest {
    @Test fun failing_track_call_returns_null_and_sets_load_failed() = runTest {
        val m = DetailModel(BoomDetailTrackingApi())

        val result = m.load("657606146365", Carrier.CJ)

        assertNull(result)
        assertTrue(m.loadFailed.value)
    }

    @Test fun successful_track_call_returns_result_and_leaves_load_failed_false() = runTest {
        val m = DetailModel(FakeTrackingApi())

        val result = m.load("657606146365", Carrier.CJ)

        assertNotNull(result)
        assertFalse(m.loadFailed.value)
    }
}
