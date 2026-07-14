package com.parcelkr.app.state

import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.domain.model.MonthlyCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryModel(repo: ParcelRepository, scope: CoroutineScope) {
    val monthlyCounts: StateFlow<List<MonthlyCount>> =
        repo.observeMonthlyCounts().stateIn(scope, SharingStarted.Eagerly, emptyList())
}
