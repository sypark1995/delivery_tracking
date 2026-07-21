package com.parcelkr.app.state

import com.parcelkr.app.data.ForwardingParcelRepository
import com.parcelkr.app.domain.CarrierDetector
import com.parcelkr.app.domain.CsvExporter
import com.parcelkr.app.domain.OverseasTrackingApi
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.ForwardingParcel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

fun searchForwardingParcels(all: List<ForwardingParcel>, query: String): List<ForwardingParcel> {
    if (query.isBlank()) return all
    val q = query.trim().lowercase()
    return all.filter {
        it.itemName.lowercase().contains(q) ||
            it.overseasTrackingNumber.lowercase().contains(q) ||
            (it.overseasCarrierName?.lowercase()?.contains(q) ?: false) ||
            (it.domesticTrackingNumber?.lowercase()?.contains(q) ?: false)
    }
}

fun distinctForwardingTags(all: List<ForwardingParcel>): List<String> = all.mapNotNull { it.tag }.distinct().sorted()

fun filterForwardingByTag(all: List<ForwardingParcel>, tag: String?): List<ForwardingParcel> =
    if (tag == null) all else all.filter { it.tag == tag }

class ForwardingModel(
    private val repo: ForwardingParcelRepository,
    private val overseasApi: OverseasTrackingApi,
    private val domesticApi: TrackingApi,
    private val detector: CarrierDetector,
    private val exporter: CsvExporter,
    private val scope: CoroutineScope,
) {
    val parcels: StateFlow<List<ForwardingParcel>> =
        repo.observeAll().stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val _addFailed = MutableStateFlow(false)
    val addFailed: StateFlow<Boolean> = _addFailed.asStateFlow()

    private val _attachFailed = MutableStateFlow(false)
    val attachFailed: StateFlow<Boolean> = _attachFailed.asStateFlow()

    suspend fun addForwardingParcel(itemName: String, overseasTrackingNumber: String): Long? {
        val result = try {
            overseasApi.register(overseasTrackingNumber)
            val r = overseasApi.track(overseasTrackingNumber)
            _addFailed.value = false
            r
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            _addFailed.value = true
            return null
        }
        return repo.add(itemName, overseasTrackingNumber, result.carrierName, result.status)
    }

    suspend fun attachDomestic(id: Long, trackingNumber: String): Boolean {
        val carrier = detector.detect(trackingNumber).carrier
        try {
            domesticApi.track(trackingNumber, carrier)
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            _attachFailed.value = true
            return false
        }
        _attachFailed.value = false
        repo.attachDomestic(id, trackingNumber, carrier)
        return true
    }

    fun delete(id: Long) {
        scope.launch { repo.delete(id) }
    }

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun setQuery(q: String) { _query.value = q }

    private val _tagFilter = MutableStateFlow<String?>(null)
    val tagFilter: StateFlow<String?> = _tagFilter.asStateFlow()

    fun setTagFilter(tag: String?) { _tagFilter.value = tag }

    fun setTag(id: Long, tag: String?) {
        scope.launch { repo.setTag(id, tag) }
    }

    fun exportCsv() {
        scope.launch {
            val parcels = repo.observeAll().first()
            exporter.exportForwarding(parcels, "parcelkr_forwarding.csv")
        }
    }
}
