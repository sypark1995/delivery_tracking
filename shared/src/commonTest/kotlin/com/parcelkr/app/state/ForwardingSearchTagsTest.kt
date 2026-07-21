package com.parcelkr.app.state

import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.ForwardingParcel
import kotlin.test.Test
import kotlin.test.assertEquals

class ForwardingSearchTagsTest {
    private fun p(
        id: Long,
        itemName: String = "item$id",
        overseasTrackingNumber: String = "T$id",
        overseasCarrierName: String? = "Carrier$id",
        domesticTrackingNumber: String? = null,
        tag: String? = null,
    ) = ForwardingParcel(
        id = id,
        itemName = itemName,
        overseasTrackingNumber = overseasTrackingNumber,
        overseasCarrierName = overseasCarrierName,
        overseasStatus = DeliveryStatus.IN_TRANSIT,
        domesticTrackingNumber = domesticTrackingNumber,
        tag = tag,
    )

    @Test fun search_matches_item_name_case_insensitive() {
        val all = listOf(p(1, itemName = "Nike Air Max"), p(2, itemName = "Adidas Shoes"))
        assertEquals(listOf(1L), searchForwardingParcels(all, "nike").map { it.id })
    }

    @Test fun search_matches_overseas_tracking_number() {
        val all = listOf(p(1, overseasTrackingNumber = "RB123456789CN"), p(2, overseasTrackingNumber = "EE999"))
        assertEquals(listOf(1L), searchForwardingParcels(all, "rb123").map { it.id })
    }

    @Test fun search_matches_overseas_carrier_name() {
        val all = listOf(p(1, overseasCarrierName = "China Post"), p(2, overseasCarrierName = "USPS"))
        assertEquals(listOf(1L), searchForwardingParcels(all, "china").map { it.id })
    }

    @Test fun search_matches_domestic_tracking_number() {
        val all = listOf(p(1, domesticTrackingNumber = "657606146365"), p(2, domesticTrackingNumber = null))
        assertEquals(listOf(1L), searchForwardingParcels(all, "657606").map { it.id })
    }

    @Test fun blank_query_returns_everything() {
        val all = listOf(p(1), p(2))
        assertEquals(all, searchForwardingParcels(all, ""))
    }

    @Test fun distinct_forwarding_tags_excludes_nulls_dedupes_and_sorts() {
        val all = listOf(p(1, tag = "ShopB"), p(2, tag = null), p(3, tag = "ShopA"), p(4, tag = "ShopB"))
        assertEquals(listOf("ShopA", "ShopB"), distinctForwardingTags(all))
    }

    @Test fun null_tag_filter_returns_everything() {
        val all = listOf(p(1, tag = "ShopA"), p(2, tag = null))
        assertEquals(all, filterForwardingByTag(all, null))
    }

    @Test fun tag_filter_matches_exact_tag_only() {
        val all = listOf(p(1, tag = "ShopA"), p(2, tag = "ShopB"), p(3, tag = null))
        assertEquals(listOf(1L), filterForwardingByTag(all, "ShopA").map { it.id })
    }
}
