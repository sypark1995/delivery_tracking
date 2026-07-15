package com.parcelkr.app.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.MonthlyCount
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ParcelRepositoryTest {
    private fun repo(): ParcelRepository {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(driver)
        return ParcelRepository(ParcelDb(driver))
    }

    @Test fun add_then_observe_returns_parcel() = runTest {
        val r = repo()
        r.add("657606146365", Carrier.CJ, "Nike Air Max", DeliveryStatus.OUT_FOR_DELIVERY, "Today", 0.72f, addedAt = 1700000000000L)
        val list = r.observeParcels().first()
        assertEquals(1, list.size)
        assertEquals(Carrier.CJ, list[0].carrier)
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, list[0].status)
        assertEquals(1700000000000L, list[0].addedAt)
    }

    @Test fun delete_removes_parcel() = runTest {
        val r = repo()
        val id = r.add("1", Carrier.CJ, "x", DeliveryStatus.RECEIVED, null, 0f)
        r.delete(id)
        assertEquals(0, r.observeParcels().first().size)
    }

    @Test fun customs_code_roundtrips() = runTest {
        val r = repo()
        assertNull(r.customsCode())
        r.setCustomsCode("P12345678901")
        assertEquals("P12345678901", r.customsCode())
    }

    @Test fun onboarding_done_defaults_to_false() = runTest {
        val r = repo()
        assertEquals(false, r.isOnboardingDone())
    }

    @Test fun onboarding_done_becomes_true_after_set() = runTest {
        val r = repo()
        r.setOnboardingDone()
        assertEquals(true, r.isOnboardingDone())
    }

    @Test fun lang_defaults_to_null() = runTest {
        val r = repo()
        assertNull(r.savedLang())
    }

    @Test fun lang_roundtrips_after_set() = runTest {
        val r = repo()
        r.setLang("ko")
        assertEquals("ko", r.savedLang())
    }

    @Test fun dark_mode_defaults_to_false() = runTest {
        val r = repo()
        assertEquals(false, r.isDarkMode())
    }

    @Test fun dark_mode_becomes_true_after_set() = runTest {
        val r = repo()
        r.setDarkMode(true)
        assertEquals(true, r.isDarkMode())
    }

    @Test fun observe_monthly_counts_groups_by_month_newest_first() = runTest {
        val r = repo()
        r.add("111", Carrier.CJ, "Item A", DeliveryStatus.DELIVERED, null, 1f, addedAt = 1781481600000L)
        r.add("222", Carrier.CJ, "Item B", DeliveryStatus.DELIVERED, null, 1f, addedAt = 1784505600000L)
        r.add("333", Carrier.CJ, "Item C", DeliveryStatus.DELIVERED, null, 1f, addedAt = 1784678400000L)
        val counts = r.observeMonthlyCounts().first()
        assertEquals(listOf(MonthlyCount("2026-07", 2), MonthlyCount("2026-06", 1)), counts)
    }

    @Test fun observe_monthly_counts_empty_when_no_parcels() = runTest {
        val r = repo()
        assertEquals(emptyList(), r.observeMonthlyCounts().first())
    }

    @Test fun update_status_changes_stored_parcel() = runTest {
        val r = repo()
        val id = r.add("111", Carrier.CJ, "Item A", DeliveryStatus.RECEIVED, null, 0.1f)
        r.updateStatus(id, DeliveryStatus.OUT_FOR_DELIVERY, "Today", 0.9f)
        val list = r.observeParcels().first()
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, list[0].status)
        assertEquals("Today", list[0].etaText)
        assertEquals(0.9f, list[0].progress)
    }

    @Test fun notifications_enabled_defaults_to_false_then_roundtrips() = runTest {
        val r = repo()
        assertEquals(false, r.notificationsEnabled())
        r.setNotificationsEnabled(true)
        assertEquals(true, r.notificationsEnabled())
    }
}
