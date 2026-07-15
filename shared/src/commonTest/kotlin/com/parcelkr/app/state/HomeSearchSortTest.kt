package com.parcelkr.app.state

import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeSearchSortTest {
    private fun p(id: Long, itemName: String, carrier: Carrier = Carrier.CJ, addedAt: Long = 0L) =
        Parcel(id, "TRACK$id", carrier, itemName, DeliveryStatus.IN_TRANSIT, null, 0f, addedAt)

    @Test fun blank_query_returns_all() {
        val all = listOf(p(1, "Nike Air Max"), p(2, "iPhone case"))
        assertEquals(all, searchParcels(all, ""))
        assertEquals(all, searchParcels(all, "   "))
    }

    @Test fun query_matches_item_name_case_insensitively() {
        val all = listOf(p(1, "Nike Air Max"), p(2, "iPhone case"))
        assertEquals(listOf(1L), searchParcels(all, "nike").map { it.id })
        assertEquals(listOf(1L), searchParcels(all, "AIR").map { it.id })
    }

    @Test fun query_matches_tracking_number() {
        val all = listOf(p(1, "Nike Air Max"), p(2, "iPhone case"))
        assertEquals(listOf(2L), searchParcels(all, "TRACK2").map { it.id })
    }

    @Test fun query_matches_carrier_display_name() {
        val all = listOf(p(1, "Nike Air Max", carrier = Carrier.CJ), p(2, "iPhone case", carrier = Carrier.HANJIN))
        assertEquals(listOf(2L), searchParcels(all, "hanjin").map { it.id })
    }

    @Test fun query_with_no_match_returns_empty() {
        val all = listOf(p(1, "Nike Air Max"))
        assertEquals(emptyList(), searchParcels(all, "zzz"))
    }

    @Test fun sort_by_recent_orders_newest_addedAt_first() {
        val all = listOf(p(1, "A", addedAt = 100L), p(2, "B", addedAt = 300L), p(3, "C", addedAt = 200L))
        assertEquals(listOf(2L, 3L, 1L), sortParcels(all, SortOrder.RECENT).map { it.id })
    }

    @Test fun sort_by_name_orders_alphabetically_case_insensitively() {
        val all = listOf(p(1, "banana"), p(2, "Apple"), p(3, "cherry"))
        assertEquals(listOf(2L, 1L, 3L), sortParcels(all, SortOrder.NAME).map { it.id })
    }
}
