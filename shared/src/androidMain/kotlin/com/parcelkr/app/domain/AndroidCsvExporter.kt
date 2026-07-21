package com.parcelkr.app.domain

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.parcelkr.app.domain.model.ForwardingParcel
import com.parcelkr.app.domain.model.Parcel
import java.io.File
import java.time.Instant
import java.time.ZoneId

class AndroidCsvExporter(private val context: Context) : CsvExporter {
    override fun export(parcels: List<Parcel>, fileName: String) {
        val csv = buildParcelCsv(parcels) { millis ->
            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().toString()
        }
        share(csv, fileName)
    }

    override fun exportForwarding(parcels: List<ForwardingParcel>, fileName: String) {
        val csv = buildForwardingParcelCsv(parcels) { millis ->
            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().toString()
        }
        share(csv, fileName)
    }

    private fun share(csv: String, fileName: String) {
        val file = File(context.cacheDir, fileName)
        file.writeText(csv)
        val uri = FileProvider.getUriForFile(context, "com.parcelkr.app.fileprovider", file)
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(sendIntent, fileName).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}
