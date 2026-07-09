package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.TrackingResult

interface TrackingApi {
    suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult
}
