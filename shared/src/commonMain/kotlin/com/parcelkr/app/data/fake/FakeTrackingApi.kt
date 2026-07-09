package com.parcelkr.app.data.fake

import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.StatusEvent
import com.parcelkr.app.domain.model.TrackingResult

class FakeTrackingApi : TrackingApi {
    override suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult =
        TrackingResult(
            trackingNumber = trackingNumber,
            carrier = carrier,
            itemName = "Nike Air Max",
            status = DeliveryStatus.OUT_FOR_DELIVERY,
            etaText = "Today, 2 – 4 PM",
            progress = 0.72f,
            events = listOf(
                StatusEvent(DeliveryStatus.OUT_FOR_DELIVERY, "배송출발", "Today 08:15", "Gangnam"),
                StatusEvent(DeliveryStatus.IN_TRANSIT, "간선하차", "Jul 9 18:40", "Okcheon"),
                StatusEvent(DeliveryStatus.RECEIVED, "집화처리", "Jul 9 10:20", "Busan"),
            ),
            driverName = "Kim Minsu",
            driverPhone = "010-1234-5678",
        )
}
