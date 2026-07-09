package com.parcelkr.app.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val KEY_CUSTOMS = "customs_code"

class ParcelRepository(private val db: ParcelDb) {
    private val q = db.parcelQueries

    fun observeParcels(): Flow<List<Parcel>> =
        q.selectAll().asFlow().mapToList(Dispatchers.Default).map { rows ->
            rows.map { row ->
                Parcel(
                    id = row.id,
                    trackingNumber = row.trackingNumber,
                    carrier = runCatching { Carrier.valueOf(row.carrier) }.getOrDefault(Carrier.UNKNOWN),
                    itemName = row.itemName,
                    status = runCatching { DeliveryStatus.valueOf(row.status) }.getOrDefault(DeliveryStatus.RECEIVED),
                    etaText = row.etaText,
                    progress = row.progress.toFloat(),
                )
            }
        }

    suspend fun add(
        trackingNumber: String, carrier: Carrier, itemName: String,
        status: DeliveryStatus, etaText: String?, progress: Float,
    ): Long {
        q.insertParcel(trackingNumber, carrier.name, itemName, status.name, etaText, progress.toDouble())
        return q.lastInsertRowId().executeAsOne()
    }

    suspend fun delete(id: Long) = q.deleteParcel(id)

    fun customsCode(): String? = q.getSetting(KEY_CUSTOMS).executeAsOneOrNull()

    suspend fun setCustomsCode(code: String) = q.putSetting(KEY_CUSTOMS, code)
}
