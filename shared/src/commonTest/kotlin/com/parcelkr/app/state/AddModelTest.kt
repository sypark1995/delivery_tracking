package com.parcelkr.app.state

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.data.fake.FakeCarrierDetector
import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.TrackingResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private class BoomTrackingApi : TrackingApi {
    override suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult =
        throw RuntimeException("boom")
}

class AddModelTest {
    private fun repo(): ParcelRepository {
        val d = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(d)
        return ParcelRepository(ParcelDb(d))
    }

    @Test fun blank_input_does_not_add() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi())
        m.onInput("   ")
        assertNull(m.confirmAdd())
        assertEquals(0, r.observeParcels().first().size)
    }

    @Test fun valid_input_adds_with_detected_carrier() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi())
        m.onInput("657606146365")
        val id = m.confirmAdd()
        assertEquals(1, r.observeParcels().first().size)
        assertEquals(id, r.observeParcels().first()[0].id)
    }

    @Test fun paste_email_with_tracking_number_prefills_input_and_detects_carrier() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi())
        val email = """
            [쿠팡] 주문이 완료되었습니다.
            주문일자: 2026-07-09
            결제금액: ${'$'}129.99
            운송장번호: 1234-5678-9012
        """.trimIndent()

        m.onPasteEmail(email)

        assertEquals("123456789012", m.input.value)
        assertEquals(true, m.guess.value?.confident)
        assertFalse(m.pasteFailed.value)
    }

    @Test fun paste_email_without_tracking_number_sets_paste_failed_and_leaves_input() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi())
        m.onInput("657606146365")

        m.onPasteEmail("Hi there, thanks for shopping with us, your order will arrive soon.")

        assertTrue(m.pasteFailed.value)
        assertEquals("657606146365", m.input.value)
    }

    @Test fun editing_input_directly_resets_paste_failed() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi())
        m.onPasteEmail("no tracking number here")
        assertTrue(m.pasteFailed.value)

        m.onInput("657606146365")

        assertFalse(m.pasteFailed.value)
    }

    @Test fun failing_track_call_sets_tracking_failed_and_confirm_add_returns_null_without_persisting() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), BoomTrackingApi())
        m.onInput("657606146365")

        val id = m.confirmAdd()

        assertNull(id)
        assertTrue(m.trackingFailed.value)
        assertEquals(0, r.observeParcels().first().size)
    }

    @Test fun editing_input_after_tracking_failure_resets_tracking_failed() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), BoomTrackingApi())
        m.onInput("657606146365")
        m.confirmAdd()
        assertTrue(m.trackingFailed.value)

        m.onInput("111111111111")

        assertFalse(m.trackingFailed.value)
    }

    @Test fun manual_carrier_selection_overrides_detected_carrier_on_confirm() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi())
        m.onInput("657606146365")
        assertEquals(Carrier.CJ, m.guess.value?.carrier)

        m.selectCarrier(Carrier.KOREA_POST)
        m.confirmAdd()

        val parcels = r.observeParcels().first()
        assertEquals(1, parcels.size)
        assertEquals(Carrier.KOREA_POST, parcels[0].carrier)
    }

    @Test fun successful_confirm_add_clears_input_so_a_second_add_starts_blank() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi())
        m.onInput("657606146365")

        m.confirmAdd()

        assertEquals("", m.input.value)
        assertNull(m.guess.value)
        assertNull(m.manualCarrier.value)
    }

    @Test fun editing_input_after_manual_selection_resets_manual_carrier() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi())
        m.onInput("657606146365")
        m.selectCarrier(Carrier.KOREA_POST)
        assertEquals(Carrier.KOREA_POST, m.manualCarrier.value)

        m.onInput("111111111111")

        assertNull(m.manualCarrier.value)
    }
}
