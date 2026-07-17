package com.parcelkr.app.state

import com.parcelkr.app.domain.OverseasTrackingApi
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.OverseasTrackingResult
import com.parcelkr.app.domain.model.TrackingResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForwardingDetailModel(
    private val overseasApi: OverseasTrackingApi,
    private val domesticApi: TrackingApi,
) {
    private val _overseasLoadFailed = MutableStateFlow(false)
    val overseasLoadFailed: StateFlow<Boolean> = _overseasLoadFailed.asStateFlow()

    private val _domesticLoadFailed = MutableStateFlow(false)
    val domesticLoadFailed: StateFlow<Boolean> = _domesticLoadFailed.asStateFlow()

    suspend fun loadOverseas(trackingNumber: String): OverseasTrackingResult? {
        return try {
            val result = overseasApi.track(trackingNumber)
            _overseasLoadFailed.value = false
            result
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            _overseasLoadFailed.value = true
            null
        }
    }

    suspend fun loadDomestic(trackingNumber: String, carrier: Carrier): TrackingResult? {
        return try {
            val result = domesticApi.track(trackingNumber, carrier)
            _domesticLoadFailed.value = false
            result
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            _domesticLoadFailed.value = true
            null
        }
    }
}
