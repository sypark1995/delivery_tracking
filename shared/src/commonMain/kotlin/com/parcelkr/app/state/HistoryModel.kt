package com.parcelkr.app.state

import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.domain.CsvExporter
import com.parcelkr.app.domain.model.MonthlyCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryModel(private val repo: ParcelRepository, private val exporter: CsvExporter, private val scope: CoroutineScope) {
    val monthlyCounts: StateFlow<List<MonthlyCount>> =
        repo.observeMonthlyCounts().stateIn(scope, SharingStarted.Eagerly, emptyList())

    fun exportCsv() {
        scope.launch {
            val parcels = repo.observeParcels().first()
            exporter.export(parcels, "parcelkr_history.csv")
        }
    }
}
