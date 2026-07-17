package com.parcelkr.app.domain

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.data.ForwardingParcelRepository
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.OverseasEvent
import com.parcelkr.app.domain.model.OverseasTrackingResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class ScriptedOverseasApi(private val resultsByNumber: Map<String, OverseasTrackingResult>) : OverseasTrackingApi {
    var callCount = 0
        private set

    override suspend fun register(trackingNumber: String) = Unit

    override suspend fun track(trackingNumber: String): OverseasTrackingResult {
        callCount++
        return resultsByNumber.getValue(trackingNumber)
    }
}

private fun result(status: DeliveryStatus) = OverseasTrackingResult(
    trackingNumber = "RB1", carrierName = "China Post", status = status,
    events = listOf(OverseasEvent(status, "desc", "time", null)),
)

class ForwardingRefresherTest {
    private fun repo(): ForwardingParcelRepository {
        val d = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(d)
        return ForwardingParcelRepository(ParcelDb(d))
    }

    @Test fun no_change_returns_empty_list_and_leaves_status_untouched() = runTest {
        val r = repo()
        r.add("Item", "RB1", "China Post", DeliveryStatus.IN_TRANSIT)
        val api = ScriptedOverseasApi(mapOf("RB1" to result(DeliveryStatus.IN_TRANSIT)))

        val changes = ForwardingRefresher(r, api).refresh()

        assertTrue(changes.isEmpty())
        assertEquals(DeliveryStatus.IN_TRANSIT, r.observeAll().first()[0].overseasStatus)
    }

    @Test fun status_change_updates_repo_and_is_reported() = runTest {
        val r = repo()
        val id = r.add("Item", "RB1", "China Post", DeliveryStatus.IN_TRANSIT)
        val api = ScriptedOverseasApi(mapOf("RB1" to result(DeliveryStatus.DELIVERED)))

        val changes = ForwardingRefresher(r, api).refresh()

        assertEquals(1, changes.size)
        assertEquals(id, changes[0].parcel.id)
        assertEquals(DeliveryStatus.DELIVERED, changes[0].newStatus)
        assertEquals(DeliveryStatus.DELIVERED, r.observeAll().first()[0].overseasStatus)
    }

    @Test fun delivered_forwarding_parcels_are_never_queried() = runTest {
        val r = repo()
        r.add("Item", "RB1", "China Post", DeliveryStatus.DELIVERED)
        val api = ScriptedOverseasApi(emptyMap())

        val changes = ForwardingRefresher(r, api).refresh()

        assertTrue(changes.isEmpty())
        assertEquals(0, api.callCount)
    }

    @Test fun a_failing_lookup_is_skipped_without_stopping_the_others() = runTest {
        val r = repo()
        r.add("Bad", "BAD", "China Post", DeliveryStatus.IN_TRANSIT)
        r.add("Good", "GOOD", "China Post", DeliveryStatus.IN_TRANSIT)
        val api = ScriptedOverseasApi(mapOf("GOOD" to result(DeliveryStatus.DELIVERED)))

        val changes = ForwardingRefresher(r, api).refresh()

        assertEquals(1, changes.size)
        assertEquals("GOOD", changes[0].parcel.overseasTrackingNumber)
    }
}
