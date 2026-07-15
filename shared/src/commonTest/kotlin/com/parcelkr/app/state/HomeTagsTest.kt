package com.parcelkr.app.state

import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeTagsTest {
    private fun p(id: Long, tag: String?) =
        Parcel(id, "T$id", Carrier.CJ, "item$id", DeliveryStatus.IN_TRANSIT, null, 0f, tag = tag)

    @Test fun distinct_tags_excludes_nulls_dedupes_and_sorts() {
        val all = listOf(p(1, "ShopB"), p(2, null), p(3, "ShopA"), p(4, "ShopB"))
        assertEquals(listOf("ShopA", "ShopB"), distinctTags(all))
    }

    @Test fun distinct_tags_empty_when_no_tags_set() {
        val all = listOf(p(1, null), p(2, null))
        assertEquals(emptyList(), distinctTags(all))
    }

    @Test fun null_tag_filter_returns_everything() {
        val all = listOf(p(1, "ShopA"), p(2, null))
        assertEquals(all, filterByTag(all, null))
    }

    @Test fun tag_filter_matches_exact_tag_only() {
        val all = listOf(p(1, "ShopA"), p(2, "ShopB"), p(3, null))
        assertEquals(listOf(1L), filterByTag(all, "ShopA").map { it.id })
    }
}
