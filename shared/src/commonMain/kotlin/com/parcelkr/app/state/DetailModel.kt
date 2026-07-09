package com.parcelkr.app.state

import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.TrackingResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailModel(
    private val api: TrackingApi,
    private val scope: CoroutineScope,
) {
    private val _showOriginal = MutableStateFlow(false)
    val showOriginal: StateFlow<Boolean> = _showOriginal.asStateFlow()

    fun toggleOriginal() { _showOriginal.value = !_showOriginal.value }

    suspend fun load(trackingNumber: String, carrier: Carrier): TrackingResult =
        api.track(trackingNumber, carrier)
}
