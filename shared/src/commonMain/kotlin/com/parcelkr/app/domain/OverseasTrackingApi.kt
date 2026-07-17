package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.OverseasTrackingResult

interface OverseasTrackingApi {
    suspend fun register(trackingNumber: String)
    suspend fun track(trackingNumber: String): OverseasTrackingResult
}
