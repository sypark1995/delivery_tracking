package com.parcelkr.app.data.real

import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.OverseasEvent
import com.parcelkr.app.domain.model.OverseasTrackingResult

object Track17Mapper {

    fun statusFor(stage: String?): DeliveryStatus = when (stage) {
        "NotFound", "InfoReceived" -> DeliveryStatus.RECEIVED
        "InTransit" -> DeliveryStatus.IN_TRANSIT
        "Expired", "AvailableForPickup" -> DeliveryStatus.DELAYED
        "OutForDelivery" -> DeliveryStatus.OUT_FOR_DELIVERY
        "DeliveryFailure", "Exception" -> DeliveryStatus.FAILED
        "Delivered" -> DeliveryStatus.DELIVERED
        else -> DeliveryStatus.RECEIVED
    }

    // ISO-8601 like "2026-07-09T14:20:00+08:00" -> "2026-07-09 14:20". Null-safe.
    fun formatTime(raw: String?): String {
        if (raw == null || raw.length < 16) return raw.orEmpty()
        return raw.take(16).replace('T', ' ')
    }

    fun toOverseasResult(accepted: Track17Accepted): OverseasTrackingResult {
        val info = accepted.trackInfo
        val provider = info?.tracking?.providers?.firstOrNull()
        return OverseasTrackingResult(
            trackingNumber = accepted.number,
            carrierName = provider?.provider?.name,
            status = statusFor(info?.latestStatus?.status),
            events = provider?.events.orEmpty().map { e ->
                OverseasEvent(
                    status = statusFor(e.stage),
                    description = e.description.orEmpty(),
                    timeText = formatTime(e.timeIso),
                    location = e.location,
                )
            },
        )
    }
}
