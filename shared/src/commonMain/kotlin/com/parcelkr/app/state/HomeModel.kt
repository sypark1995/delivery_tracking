package com.parcelkr.app.state

import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Segment { ACTIVE, DELIVERED, ALL }
enum class SortOrder { RECENT, NAME }

fun filterBySegment(all: List<Parcel>, seg: Segment): List<Parcel> = when (seg) {
    Segment.ALL -> all
    Segment.DELIVERED -> all.filter { it.status == DeliveryStatus.DELIVERED }
    Segment.ACTIVE -> all.filter { it.status != DeliveryStatus.DELIVERED }
}

fun heroOf(all: List<Parcel>): Parcel? =
    all.firstOrNull { it.status != DeliveryStatus.DELIVERED }

fun searchParcels(all: List<Parcel>, query: String): List<Parcel> {
    if (query.isBlank()) return all
    val q = query.trim().lowercase()
    return all.filter {
        it.itemName.lowercase().contains(q) ||
            it.trackingNumber.lowercase().contains(q) ||
            it.carrier.displayName.lowercase().contains(q)
    }
}

fun sortParcels(all: List<Parcel>, sort: SortOrder): List<Parcel> = when (sort) {
    SortOrder.RECENT -> all.sortedByDescending { it.addedAt }
    SortOrder.NAME -> all.sortedBy { it.itemName.lowercase() }
}

fun distinctTags(all: List<Parcel>): List<String> = all.mapNotNull { it.tag }.distinct().sorted()

fun filterByTag(all: List<Parcel>, tag: String?): List<Parcel> = if (tag == null) all else all.filter { it.tag == tag }

class HomeModel(private val repo: ParcelRepository, private val scope: CoroutineScope) {
    val parcels: StateFlow<List<Parcel>> =
        repo.observeParcels().stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val _segment = MutableStateFlow(Segment.ACTIVE)
    val segment: StateFlow<Segment> = _segment.asStateFlow()

    fun setSegment(s: Segment) { _segment.value = s }

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun setQuery(q: String) { _query.value = q }

    private val _sort = MutableStateFlow(SortOrder.RECENT)
    val sort: StateFlow<SortOrder> = _sort.asStateFlow()

    fun setSort(s: SortOrder) { _sort.value = s }

    private val _tagFilter = MutableStateFlow<String?>(null)
    val tagFilter: StateFlow<String?> = _tagFilter.asStateFlow()

    fun setTagFilter(tag: String?) { _tagFilter.value = tag }

    fun setTag(id: Long, tag: String?) {
        scope.launch { repo.setTag(id, tag) }
    }

    fun delete(id: Long) {
        scope.launch { repo.delete(id) }
    }
}
