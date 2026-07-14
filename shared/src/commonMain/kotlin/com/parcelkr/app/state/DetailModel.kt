package com.parcelkr.app.state

import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.TrackingResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailModel(
    private val api: TrackingApi,
) {
    private val _showOriginal = MutableStateFlow(false)
    val showOriginal: StateFlow<Boolean> = _showOriginal.asStateFlow()

    private val _loadFailed = MutableStateFlow(false)
    val loadFailed: StateFlow<Boolean> = _loadFailed.asStateFlow()

    fun toggleOriginal() { _showOriginal.value = !_showOriginal.value }

    suspend fun load(trackingNumber: String, carrier: Carrier): TrackingResult? {
        return try {
            val result = api.track(trackingNumber, carrier)
            _loadFailed.value = false
            result
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            _loadFailed.value = true
            null
        }
    }
}
