package com.parcelkr.app.domain

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.StatusEvent
import com.parcelkr.app.domain.model.TrackingResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class ScriptedTrackingApi(private val resultsByTrackingNumber: Map<String, TrackingResult>) : TrackingApi {
    var callCount = 0
        private set

    override suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult {
        callCount++
        return resultsByTrackingNumber.getValue(trackingNumber)
    }
}

private class ThrowingTrackingApi : TrackingApi {
    override suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult =
        throw RuntimeException("network error")
}

private fun result(status: DeliveryStatus) = TrackingResult(
    trackingNumber = "T", carrier = Carrier.CJ, itemName = "Item",
    status = status, etaText = "ETA", progress = 0.5f,
    events = listOf(StatusEvent(status, "라벨", "time", "place")),
    driverName = null, driverPhone = null,
)

class ParcelRefresherTest {
    private fun repo(): ParcelRepository {
        val d = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(d)
        return ParcelRepository(ParcelDb(d))
    }

    @Test fun no_change_returns_empty_list_and_leaves_status_untouched() = runTest {
        val r = repo()
        r.add("A1", Carrier.CJ, "Item A", DeliveryStatus.IN_TRANSIT, null, 0.3f)
        val api = ScriptedTrackingApi(mapOf("A1" to result(DeliveryStatus.IN_TRANSIT)))
        val refresher = ParcelRefresher(r, api)

        val changes = refresher.refresh()

        assertTrue(changes.isEmpty())
        assertEquals(DeliveryStatus.IN_TRANSIT, r.observeParcels().first()[0].status)
    }

    @Test fun status_change_updates_repo_and_is_reported() = runTest {
        val r = repo()
        val id = r.add("A1", Carrier.CJ, "Item A", DeliveryStatus.IN_TRANSIT, null, 0.3f)
        val api = ScriptedTrackingApi(mapOf("A1" to result(DeliveryStatus.OUT_FOR_DELIVERY)))
        val refresher = ParcelRefresher(r, api)

        val changes = refresher.refresh()

        assertEquals(1, changes.size)
        assertEquals(id, changes[0].parcel.id)
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, changes[0].newStatus)
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, r.observeParcels().first()[0].status)
    }

    @Test fun delivered_parcels_are_never_queried() = runTest {
        val r = repo()
        r.add("A1", Carrier.CJ, "Item A", DeliveryStatus.DELIVERED, null, 1f)
        val api = ScriptedTrackingApi(emptyMap())
        val refresher = ParcelRefresher(r, api)

        val changes = refresher.refresh()

        assertTrue(changes.isEmpty())
        assertEquals(0, api.callCount)
    }

    @Test fun a_failing_lookup_is_skipped_without_stopping_the_others() = runTest {
        val r = repo()
        r.add("BAD", Carrier.CJ, "Item Bad", DeliveryStatus.IN_TRANSIT, null, 0.3f)
        r.add("GOOD", Carrier.CJ, "Item Good", DeliveryStatus.IN_TRANSIT, null, 0.3f)
        val api = ScriptedTrackingApi(mapOf("GOOD" to result(DeliveryStatus.DELIVERED)))
        // ScriptedTrackingApi throws NoSuchElementException (Map.getValue) for "BAD" since it's not in the map — this exercises the same non-cancellation-exception path as ThrowingTrackingApi.
        val refresher = ParcelRefresher(r, api)

        val changes = refresher.refresh()

        assertEquals(1, changes.size)
        assertEquals("GOOD", changes[0].parcel.trackingNumber)
    }
}
