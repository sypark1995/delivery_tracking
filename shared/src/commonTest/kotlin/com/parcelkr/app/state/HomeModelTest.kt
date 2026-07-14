package com.parcelkr.app.state

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
        val m = HomeModel(r, backgroundScope)

        m.delete(id)

        // parcels is a hot reactive flow driven by repo.observeParcels(); wait for it to reflect the delete
        // rather than racing a single snapshot read against the async query pipeline.
        val remaining = m.parcels.first { list -> list.isNotEmpty() && list.none { it.id == id } }
        assertEquals(listOf("111"), remaining.map { it.trackingNumber })
    }
}
