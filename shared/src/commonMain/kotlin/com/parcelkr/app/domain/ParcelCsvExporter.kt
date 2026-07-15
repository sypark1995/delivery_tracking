package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.Parcel

private fun csvEscape(field: String): String =
    if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
        "\"" + field.replace("\"", "\"\"") + "\""
    } else {
        field
    }

fun buildParcelCsv(parcels: List<Parcel>, formatDate: (Long) -> String): String {
    val header = "TrackingNumber,Carrier,ItemName,Status,AddedAt,Tag"
    val rows = parcels.map { p ->
        listOf(
            p.trackingNumber,
            p.carrier.displayName,
            p.itemName,
            p.status.name,
            formatDate(p.addedAt),
            p.tag.orEmpty(),
        ).joinToString(",") { csvEscape(it) }
    }
    return (listOf(header) + rows).joinToString("\n")
}

interface CsvExporter {
    fun export(parcels: List<Parcel>, fileName: String)
}
