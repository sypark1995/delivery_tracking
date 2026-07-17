package com.parcelkr.app.domain

import com.parcelkr.app.data.ForwardingParcelRepository
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.ForwardingParcel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first

data class ForwardingStatusChange(val parcel: ForwardingParcel, val newStatus: DeliveryStatus)

class ForwardingRefresher(private val repo: ForwardingParcelRepository, private val api: OverseasTrackingApi) {
    suspend fun refresh(): List<ForwardingStatusChange> {
        val active = repo.observeAll().first().filter { it.overseasStatus != DeliveryStatus.DELIVERED }
        val changes = mutableListOf<ForwardingStatusChange>()
        for (parcel in active) {
            val result = try {
                api.track(parcel.overseasTrackingNumber)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                continue
            }
            if (result.status != parcel.overseasStatus) {
                repo.updateOverseasStatus(parcel.id, result.status)
                changes.add(ForwardingStatusChange(parcel, result.status))
            }
        }
        return changes
    }
}
