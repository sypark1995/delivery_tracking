package com.parcelkr.app.state

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.data.ForwardingParcelRepository
import com.parcelkr.app.data.fake.FakeCarrierDetector
import com.parcelkr.app.data.fake.FakeOverseasTrackingApi
import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.OverseasTrackingApi
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.OverseasTrackingResult
import com.parcelkr.app.domain.model.TrackingResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private class BoomOverseasApi : OverseasTrackingApi {
    override suspend fun register(trackingNumber: String) = Unit
    override suspend fun track(trackingNumber: String): OverseasTrackingResult = throw RuntimeException("boom")
}

private class BoomDomesticTrackingApi : TrackingApi {
    override suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult = throw RuntimeException("boom")
}

class ForwardingModelTest {
    private fun repo(): ForwardingParcelRepository {
        val d = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(d)
        return ForwardingParcelRepository(ParcelDb(d))
    }

    @Test fun add_forwarding_parcel_registers_and_tracks_then_persists() = runTest {
        val r = repo()
        val m = ForwardingModel(r, FakeOverseasTrackingApi(), FakeTrackingApi(), FakeCarrierDetector(), backgroundScope)

        val id = m.addForwardingParcel("Nike shoes", "RB123456789CN")

        assertTrue(id != null)
        val list = m.parcels.first { it.isNotEmpty() }
        assertEquals("Nike shoes", list[0].itemName)
        assertEquals("RB123456789CN", list[0].overseasTrackingNumber)
        assertEquals(DeliveryStatus.IN_TRANSIT, list[0].overseasStatus)
        assertFalse(m.addFailed.value)
    }

    @Test fun add_forwarding_parcel_sets_add_failed_when_overseas_api_throws() = runTest {
        val r = repo()
        val m = ForwardingModel(r, BoomOverseasApi(), FakeTrackingApi(), FakeCarrierDetector(), backgroundScope)

        val id = m.addForwardingParcel("Item", "RB1")

        assertNull(id)
        assertTrue(m.addFailed.value)
        assertEquals(0, m.parcels.value.size)
    }

    @Test fun attach_domestic_detects_carrier_tracks_and_persists() = runTest {
        val r = repo()
        val m = ForwardingModel(r, FakeOverseasTrackingApi(), FakeTrackingApi(), FakeCarrierDetector(), backgroundScope)
        val id = requireNotNull(m.addForwardingParcel("Item", "RB1"))

        val success = m.attachDomestic(id, "657606146365")

        assertTrue(success)
        val list = m.parcels.first { it.firstOrNull()?.domesticTrackingNumber != null }
        assertEquals("657606146365", list[0].domesticTrackingNumber)
        assertEquals(Carrier.CJ, list[0].domesticCarrier)
    }

    @Test fun attach_domestic_returns_false_and_sets_attach_failed_when_domestic_api_throws() = runTest {
        val r = repo()
        val m = ForwardingModel(r, FakeOverseasTrackingApi(), BoomDomesticTrackingApi(), FakeCarrierDetector(), backgroundScope)
        val id = requireNotNull(m.addForwardingParcel("Item", "RB1"))

        val success = m.attachDomestic(id, "657606146365")

        assertFalse(success)
        assertTrue(m.attachFailed.value)
    }

    @Test fun attach_domestic_success_clears_attach_failed() = runTest {
        val r = repo()
        val m = ForwardingModel(r, FakeOverseasTrackingApi(), BoomDomesticTrackingApi(), FakeCarrierDetector(), backgroundScope)
        val id = requireNotNull(m.addForwardingParcel("Item", "RB1"))
        m.attachDomestic(id, "657606146365")
        assertTrue(m.attachFailed.value)

        val workingModel = ForwardingModel(r, FakeOverseasTrackingApi(), FakeTrackingApi(), FakeCarrierDetector(), backgroundScope)
        val success = workingModel.attachDomestic(id, "657606146365")

        assertTrue(success)
        assertFalse(workingModel.attachFailed.value)
    }

    @Test fun delete_removes_forwarding_parcel() = runTest {
        val r = repo()
        val m = ForwardingModel(r, FakeOverseasTrackingApi(), FakeTrackingApi(), FakeCarrierDetector(), backgroundScope)
        val id = requireNotNull(m.addForwardingParcel("Item", "RB1"))

        m.delete(id)

        val list = m.parcels.first { it.isEmpty() }
        assertEquals(0, list.size)
    }
}
