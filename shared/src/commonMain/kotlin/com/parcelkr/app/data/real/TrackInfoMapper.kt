package com.parcelkr.app.data.real

import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.StatusEvent
import com.parcelkr.app.domain.model.TrackingResult

object TrackInfoMapper {

    fun statusFor(code: String): DeliveryStatus = when (code) {
        "UNKNOWN", "INFORMATION_RECEIVED", "AT_PICKUP" -> DeliveryStatus.RECEIVED
        "IN_TRANSIT" -> DeliveryStatus.IN_TRANSIT
        "OUT_FOR_DELIVERY" -> DeliveryStatus.OUT_FOR_DELIVERY
        "ATTEMPT_FAIL", "EXCEPTION" -> DeliveryStatus.FAILED
        "DELIVERED" -> DeliveryStatus.DELIVERED
        "AVAILABLE_FOR_PICKUP" -> DeliveryStatus.DELAYED
        else -> DeliveryStatus.RECEIVED
    }

    fun progressFor(status: DeliveryStatus): Float = when (status) {
        DeliveryStatus.RECEIVED -> 0.15f
        DeliveryStatus.IN_TRANSIT -> 0.45f
        DeliveryStatus.OUT_FOR_DELIVERY -> 0.8f
        DeliveryStatus.DELAYED -> 0.6f
        DeliveryStatus.FAILED -> 0.5f
        DeliveryStatus.DELIVERED -> 1.0f
    }

    // ISO-8601 like "2026-07-02T15:11:06.000+09:00" -> "2026-07-02 15:11". Null-safe.
    fun formatTime(raw: String?): String {
        if (raw == null || raw.length < 16) return raw.orEmpty()
        return raw.take(16).replace('T', ' ')
    }

    fun toTrackingResult(dto: TrackInfoDto, carrier: Carrier): TrackingResult {
        val lastStatus = statusFor(dto.lastEvent?.status?.code ?: "UNKNOWN")
        // API returns events oldest-first; our UI expects newest-first (index 0 = current step).
        val events = dto.events?.edges.orEmpty()
            .map { edge ->
                val node = edge.node
                StatusEvent(
                    status = statusFor(node.status.code),
                    labelKo = node.status.name ?: "",
                    timeText = formatTime(node.time),
                    place = node.location?.name ?: "",
                )
            }
            .reversed()
        return TrackingResult(
            trackingNumber = dto.trackingNumber,
            carrier = carrier,
            // Delivery Tracker has no item/product-name field at all — carrier display name is
            // the least-wrong placeholder available until a future task lets users name a parcel.
            itemName = carrier.displayName,
            status = lastStatus,
            // No predicted-arrival field exists either; UI already falls back to "expected today"
            // copy when this is null.
            etaText = null,
            progress = progressFor(lastStatus),
            events = events,
            driverName = dto.lastEvent?.contact?.name,
            driverPhone = dto.lastEvent?.contact?.phoneNumber,
        )
    }
}
