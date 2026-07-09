package com.parcelkr.app.state

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.data.fake.FakeCarrierDetector
import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.db.ParcelDb
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AddModelTest {
    private fun repo(): ParcelRepository {
        val d = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(d)
        return ParcelRepository(ParcelDb(d))
    }

    @Test fun blank_input_does_not_add() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi(), this)
        m.onInput("   ")
        assertNull(m.confirmAdd())
        assertEquals(0, r.observeParcels().first().size)
    }

    @Test fun valid_input_adds_with_detected_carrier() = runTest {
        val r = repo()
        val m = AddModel(r, FakeCarrierDetector(), FakeTrackingApi(), this)
        m.onInput("657606146365")
        val id = m.confirmAdd()
        assertEquals(1, r.observeParcels().first().size)
        assertEquals(id, r.observeParcels().first()[0].id)
    }
}
