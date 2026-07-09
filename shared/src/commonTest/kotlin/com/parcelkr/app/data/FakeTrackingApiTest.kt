package com.parcelkr.app.data

import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeTrackingApiTest {
    @Test fun returns_mock_timeline_with_driver() = runTest {
        val r = FakeTrackingApi().track("657606146365", Carrier.CJ)
        assertEquals("657606146365", r.trackingNumber)
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, r.status)
        assertTrue(r.events.isNotEmpty())
        assertEquals("Kim Minsu", r.driverName)
    }
}
