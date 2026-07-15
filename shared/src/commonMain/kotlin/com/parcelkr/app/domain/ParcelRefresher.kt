package com.parcelkr.app.domain

import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first

data class StatusChange(val parcel: Parcel, val newStatus: DeliveryStatus)

class ParcelRefresher(private val repo: ParcelRepository, private val api: TrackingApi) {
    suspend fun refresh(): List<StatusChange> {
        val active = repo.observeParcels().first().filter { it.status != DeliveryStatus.DELIVERED }
        val changes = mutableListOf<StatusChange>()
        for (parcel in active) {
            val result = try {
                api.track(parcel.trackingNumber, parcel.carrier)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                continue
            }
            if (result.status != parcel.status) {
                repo.updateStatus(parcel.id, result.status, result.etaText, result.progress)
                changes.add(StatusChange(parcel, result.status))
            }
        }
        return changes
    }
}
