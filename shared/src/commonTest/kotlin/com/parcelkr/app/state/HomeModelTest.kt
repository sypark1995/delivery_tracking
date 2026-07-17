package com.parcelkr.app.state

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import com.parcelkr.app.domain.model.StatusEvent
import com.parcelkr.app.domain.model.TrackingResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

private class ScriptedTrackingApi(private val resultsByTrackingNumber: Map<String, TrackingResult>) : TrackingApi {
    override suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult =
        resultsByTrackingNumber.getValue(trackingNumber)
}

private fun result(status: DeliveryStatus) = TrackingResult(
    trackingNumber = "A1", carrier = Carrier.CJ, itemName = "Item A",
    status = status, etaText = "ETA", progress = 0.5f,
    events = listOf(StatusEvent(status, "라벨", "time", "place")),
    driverName = null, driverPhone = null,
)

class HomeModelTest {
    private fun p(id: Long, s: DeliveryStatus) =
        Parcel(id, "$id", Carrier.CJ, "item$id", s, null, 0f)

    private fun repo(): ParcelRepository {
        val d = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(d)
        return ParcelRepository(ParcelDb(d))
    }

    @Test fun active_segment_excludes_delivered() {
        val all = listOf(p(1, DeliveryStatus.OUT_FOR_DELIVERY), p(2, DeliveryStatus.DELIVERED))
        assertEquals(listOf(1L), filterBySegment(all, Segment.ACTIVE).map { it.id })
    }
    @Test fun delivered_segment_only_delivered() {
        val all = listOf(p(1, DeliveryStatus.OUT_FOR_DELIVERY), p(2, DeliveryStatus.DELIVERED))
        assertEquals(listOf(2L), filterBySegment(all, Segment.DELIVERED).map { it.id })
    }
    @Test fun hero_is_first_non_delivered() {
        val all = listOf(p(2, DeliveryStatus.DELIVERED), p(1, DeliveryStatus.OUT_FOR_DELIVERY))
        assertEquals(1L, heroOf(all)?.id)
    }

    @Test fun delete_removes_parcel_and_updates_flow() = runTest {
        val r = repo()
        r.add("111", Carrier.CJ, "item1", DeliveryStatus.OUT_FOR_DELIVERY, null, 0f)
        val id = r.add("222", Carrier.CJ, "item2", DeliveryStatus.OUT_FOR_DELIVERY, null, 0f)
        val m = HomeModel(r, FakeTrackingApi(), backgroundScope)

        m.delete(id)

        // parcels is a hot reactive flow driven by repo.observeParcels(); wait for it to reflect the delete
        // rather than racing a single snapshot read against the async query pipeline.
        val remaining = m.parcels.first { list -> list.isNotEmpty() && list.none { it.id == id } }
        assertEquals(listOf("111"), remaining.map { it.trackingNumber })
    }

    @Test fun refresh_updates_status_from_api_and_clears_refreshing_flag() = runTest {
        val r = repo()
        r.add("A1", Carrier.CJ, "Item A", DeliveryStatus.IN_TRANSIT, null, 0.3f)
        val api = ScriptedTrackingApi(mapOf("A1" to result(DeliveryStatus.OUT_FOR_DELIVERY)))
        val m = HomeModel(r, api, backgroundScope)

        m.refresh()

        val updated = m.parcels.first { list -> list.isNotEmpty() && list[0].status == DeliveryStatus.OUT_FOR_DELIVERY }
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, updated[0].status)
        assertFalse(m.refreshing.value)
    }

    @Test fun clear_delivered_removes_only_delivered_parcels_from_flow() = runTest {
        val r = repo()
        r.add("111", Carrier.CJ, "item1", DeliveryStatus.DELIVERED, null, 1f)
        r.add("222", Carrier.CJ, "item2", DeliveryStatus.OUT_FOR_DELIVERY, null, 0.5f)
        val m = HomeModel(r, FakeTrackingApi(), backgroundScope)

        m.clearDelivered()

        val remaining = m.parcels.first { list -> list.isNotEmpty() && list.none { it.status == DeliveryStatus.DELIVERED } }
        assertEquals(listOf("222"), remaining.map { it.trackingNumber })
    }
}
