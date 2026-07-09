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

class ParcelRepositoryTest {
    private fun repo(): ParcelRepository {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(driver)
        return ParcelRepository(ParcelDb(driver))
    }

    @Test fun add_then_observe_returns_parcel() = runTest {
        val r = repo()
        r.add("657606146365", Carrier.CJ, "Nike Air Max", DeliveryStatus.OUT_FOR_DELIVERY, "Today", 0.72f)
        val list = r.observeParcels().first()
        assertEquals(1, list.size)
        assertEquals(Carrier.CJ, list[0].carrier)
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, list[0].status)
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
}
