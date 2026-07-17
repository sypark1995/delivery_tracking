package com.parcelkr.app.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ForwardingParcelRepositoryTest {
    private fun repo(): ForwardingParcelRepository {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(driver)
        return ForwardingParcelRepository(ParcelDb(driver))
    }

    @Test fun add_then_observe_returns_forwarding_parcel_without_domestic_leg() = runTest {
        val r = repo()
        r.add("Nike shoes", "RB123456789CN", "China Post", DeliveryStatus.IN_TRANSIT, addedAt = 1700000000000L)

        val list = r.observeAll().first()

        assertEquals(1, list.size)
        assertEquals("Nike shoes", list[0].itemName)
        assertEquals("RB123456789CN", list[0].overseasTrackingNumber)
        assertEquals("China Post", list[0].overseasCarrierName)
        assertEquals(DeliveryStatus.IN_TRANSIT, list[0].overseasStatus)
        assertNull(list[0].domesticTrackingNumber)
        assertNull(list[0].domesticCarrier)
        assertEquals(1700000000000L, list[0].addedAt)
    }

    @Test fun update_overseas_status_changes_stored_status() = runTest {
        val r = repo()
        val id = r.add("Item", "RB1", "China Post", DeliveryStatus.RECEIVED)

        r.updateOverseasStatus(id, DeliveryStatus.DELIVERED)

        assertEquals(DeliveryStatus.DELIVERED, r.observeAll().first()[0].overseasStatus)
    }

    @Test fun attach_domestic_sets_domestic_tracking_number_and_carrier() = runTest {
        val r = repo()
        val id = r.add("Item", "RB1", "China Post", DeliveryStatus.DELIVERED)

        r.attachDomestic(id, "657606146365", Carrier.CJ)

        val stored = r.observeAll().first()[0]
        assertEquals("657606146365", stored.domesticTrackingNumber)
        assertEquals(Carrier.CJ, stored.domesticCarrier)
    }

    @Test fun delete_removes_forwarding_parcel() = runTest {
        val r = repo()
        val id = r.add("Item", "RB1", null, DeliveryStatus.RECEIVED)

        r.delete(id)

        assertEquals(0, r.observeAll().first().size)
    }
}
