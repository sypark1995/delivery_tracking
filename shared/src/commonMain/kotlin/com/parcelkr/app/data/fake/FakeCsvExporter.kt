package com.parcelkr.app.data.fake

import com.parcelkr.app.domain.CsvExporter
import com.parcelkr.app.domain.model.ForwardingParcel
import com.parcelkr.app.domain.model.Parcel
import kotlinx.coroutines.CompletableDeferred

class FakeCsvExporter : CsvExporter {
    var lastExportedFileName: String? = null
    var lastExportedForwardingFileName: String? = null
    val forwardingExported = CompletableDeferred<String>()

    override fun export(parcels: List<Parcel>, fileName: String) {
        lastExportedFileName = fileName
    }

    override fun exportForwarding(parcels: List<ForwardingParcel>, fileName: String) {
        lastExportedForwardingFileName = fileName
        forwardingExported.complete(fileName)
    }
}
