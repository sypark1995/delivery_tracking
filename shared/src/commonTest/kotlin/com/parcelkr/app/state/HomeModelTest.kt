package com.parcelkr.app.state

import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeModelTest {
    private fun p(id: Long, s: DeliveryStatus) =
        Parcel(id, "$id", Carrier.CJ, "item$id", s, null, 0f)

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
}
