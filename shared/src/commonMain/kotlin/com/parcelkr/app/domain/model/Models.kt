package com.parcelkr.app.domain.model

enum class DeliveryStatus { RECEIVED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, FAILED, DELAYED }

enum class Carrier(val displayName: String) {
    CJ("CJ Logistics"),
    KOREA_POST("Korea Post"),
    COUPANG("Coupang"),
    LOTTE("Lotte"),
    HANJIN("Hanjin"),
    UNKNOWN("Unknown"),
}

data class Parcel(
    val id: Long,
    val trackingNumber: String,
    val carrier: Carrier,
    val itemName: String,
    val status: DeliveryStatus,
    val etaText: String?,
    val progress: Float,
    val addedAt: Long = 0L,
)

data class StatusEvent(
    val status: DeliveryStatus,
    val labelKo: String,
    val timeText: String,
    val place: String,
)

data class TrackingResult(
    val trackingNumber: String,
    val carrier: Carrier,
    val itemName: String,
    val status: DeliveryStatus,
    val etaText: String?,
    val progress: Float,
    val events: List<StatusEvent>,
    val driverName: String?,
    val driverPhone: String?,
)

data class CarrierGuess(val carrier: Carrier, val confident: Boolean)

data class MonthlyCount(val month: String, val count: Long)
