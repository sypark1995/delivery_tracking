package com.parcelkr.app.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.parcelkr.app.currentTimeMillis
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.ForwardingParcel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ForwardingParcelRepository(private val db: ParcelDb) {
    private val q = db.forwardingParcelQueries

    fun observeAll(): Flow<List<ForwardingParcel>> =
        q.selectAllForwarding().asFlow().mapToList(Dispatchers.Default).map { rows ->
            rows.map { row ->
                ForwardingParcel(
                    id = row.id,
                    itemName = row.itemName,
                    overseasTrackingNumber = row.overseasTrackingNumber,
                    overseasCarrierName = row.overseasCarrierName,
                    overseasStatus = runCatching { DeliveryStatus.valueOf(row.overseasStatus) }.getOrDefault(DeliveryStatus.RECEIVED),
                    domesticTrackingNumber = row.domesticTrackingNumber,
                    domesticCarrier = row.domesticCarrier?.let { runCatching { Carrier.valueOf(it) }.getOrNull() },
                    addedAt = row.addedAt,
                    tag = row.tag,
                )
            }
        }

    suspend fun add(
        itemName: String,
        overseasTrackingNumber: String,
        overseasCarrierName: String?,
        overseasStatus: DeliveryStatus,
        addedAt: Long = currentTimeMillis(),
    ): Long {
        q.insertForwarding(itemName, overseasTrackingNumber, overseasCarrierName, overseasStatus.name, addedAt)
        return q.lastInsertForwardingRowId().executeAsOne()
    }

    suspend fun updateOverseasStatus(id: Long, status: DeliveryStatus) =
        q.updateOverseasStatus(status.name, id)

    suspend fun attachDomestic(id: Long, trackingNumber: String, carrier: Carrier) =
        q.attachDomestic(trackingNumber, carrier.name, id)

    suspend fun setTag(id: Long, tag: String?) = q.updateForwardingTag(tag, id)

    suspend fun delete(id: Long) = q.deleteForwarding(id)
}
