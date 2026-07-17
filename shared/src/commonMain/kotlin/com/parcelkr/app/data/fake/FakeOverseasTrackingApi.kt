package com.parcelkr.app.data.fake

import com.parcelkr.app.domain.OverseasTrackingApi
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.OverseasEvent
import com.parcelkr.app.domain.model.OverseasTrackingResult

class FakeOverseasTrackingApi : OverseasTrackingApi {
    override suspend fun register(trackingNumber: String) = Unit

    override suspend fun track(trackingNumber: String): OverseasTrackingResult =
        OverseasTrackingResult(
            trackingNumber = trackingNumber,
            carrierName = "China Post",
            status = DeliveryStatus.IN_TRANSIT,
            events = listOf(
                OverseasEvent(DeliveryStatus.IN_TRANSIT, "Departed from origin country", "Jul 9 14:20", "Shenzhen, China"),
                OverseasEvent(DeliveryStatus.RECEIVED, "Order information received", "Jul 8 09:00", null),
            ),
        )
}
