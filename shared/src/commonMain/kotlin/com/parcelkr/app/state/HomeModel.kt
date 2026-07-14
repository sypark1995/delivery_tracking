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

fun filterBySegment(all: List<Parcel>, seg: Segment): List<Parcel> = when (seg) {
    Segment.ALL -> all
    Segment.DELIVERED -> all.filter { it.status == DeliveryStatus.DELIVERED }
    Segment.ACTIVE -> all.filter { it.status != DeliveryStatus.DELIVERED }
}

fun heroOf(all: List<Parcel>): Parcel? =
    all.firstOrNull { it.status != DeliveryStatus.DELIVERED }

class HomeModel(private val repo: ParcelRepository, private val scope: CoroutineScope) {
    val parcels: StateFlow<List<Parcel>> =
        repo.observeParcels().stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val _segment = MutableStateFlow(Segment.ACTIVE)
    val segment: StateFlow<Segment> = _segment.asStateFlow()

    fun setSegment(s: Segment) { _segment.value = s }

    fun delete(id: Long) {
        scope.launch { repo.delete(id) }
    }
}
