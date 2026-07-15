package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import kotlin.test.Test
import kotlin.test.assertEquals

class ParcelCsvExporterTest {
    private val fixedDate: (Long) -> String = { "2026-01-0" + (it / 1000) }

    @Test fun header_only_for_empty_list() {
        assertEquals("TrackingNumber,Carrier,ItemName,Status,AddedAt,Tag", buildParcelCsv(emptyList(), fixedDate))
    }

    @Test fun renders_one_row_per_parcel() {
        val parcels = listOf(
            Parcel(1, "111", Carrier.CJ, "Nike Air Max", DeliveryStatus.DELIVERED, null, 1f, addedAt = 1000L, tag = "Family"),
        )
        val expected = "TrackingNumber,Carrier,ItemName,Status,AddedAt,Tag\n111,CJ Logistics,Nike Air Max,DELIVERED,2026-01-01,Family"
        assertEquals(expected, buildParcelCsv(parcels, fixedDate))
    }

    @Test fun blank_tag_renders_as_empty_field() {
        val parcels = listOf(
            Parcel(1, "111", Carrier.CJ, "Item", DeliveryStatus.IN_TRANSIT, null, 0.5f, addedAt = 1000L, tag = null),
        )
        val expected = "TrackingNumber,Carrier,ItemName,Status,AddedAt,Tag\n111,CJ Logistics,Item,IN_TRANSIT,2026-01-01,"
        assertEquals(expected, buildParcelCsv(parcels, fixedDate))
    }

    @Test fun escapes_field_containing_comma_with_quotes() {
        val parcels = listOf(
            Parcel(1, "111", Carrier.CJ, "Shoes, Size 10", DeliveryStatus.IN_TRANSIT, null, 0.5f, addedAt = 1000L, tag = null),
        )
        val expected = "TrackingNumber,Carrier,ItemName,Status,AddedAt,Tag\n111,CJ Logistics,\"Shoes, Size 10\",IN_TRANSIT,2026-01-01,"
        assertEquals(expected, buildParcelCsv(parcels, fixedDate))
    }

    @Test fun escapes_field_containing_quote_by_doubling_it() {
        val parcels = listOf(
            Parcel(1, "111", Carrier.CJ, "Item \"Special\"", DeliveryStatus.IN_TRANSIT, null, 0.5f, addedAt = 1000L, tag = null),
        )
        val expected = "TrackingNumber,Carrier,ItemName,Status,AddedAt,Tag\n111,CJ Logistics,\"Item \"\"Special\"\"\",IN_TRANSIT,2026-01-01,"
        assertEquals(expected, buildParcelCsv(parcels, fixedDate))
    }

    @Test fun multiple_parcels_join_with_newline() {
        val parcels = listOf(
            Parcel(1, "111", Carrier.CJ, "A", DeliveryStatus.DELIVERED, null, 1f, addedAt = 1000L),
            Parcel(2, "222", Carrier.HANJIN, "B", DeliveryStatus.IN_TRANSIT, null, 0.5f, addedAt = 2000L),
        )
        val result = buildParcelCsv(parcels, fixedDate)
        assertEquals(3, result.split("\n").size)
    }
}
