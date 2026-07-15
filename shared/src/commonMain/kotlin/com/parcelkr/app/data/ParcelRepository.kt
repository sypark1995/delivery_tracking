package com.parcelkr.app.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.parcelkr.app.currentTimeMillis
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.MonthlyCount
import com.parcelkr.app.domain.model.Parcel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val KEY_CUSTOMS = "customs_code"
private const val KEY_ONBOARDING_DONE = "onboarding_done"
private const val KEY_LANG = "lang"
private const val KEY_DARK = "dark_mode"

class ParcelRepository(private val db: ParcelDb) {
    private val q = db.parcelQueries

    fun observeParcels(): Flow<List<Parcel>> =
        q.selectAll().asFlow().mapToList(Dispatchers.Default).map { rows ->
            rows.map { row ->
                Parcel(
                    id = row.id,
                    trackingNumber = row.trackingNumber,
                    carrier = runCatching { Carrier.valueOf(row.carrier) }.getOrDefault(Carrier.UNKNOWN),
                    itemName = row.itemName,
                    status = runCatching { DeliveryStatus.valueOf(row.status) }.getOrDefault(DeliveryStatus.RECEIVED),
                    etaText = row.etaText,
                    progress = row.progress.toFloat(),
                    addedAt = row.addedAt,
                )
            }
        }

    suspend fun add(
        trackingNumber: String, carrier: Carrier, itemName: String,
        status: DeliveryStatus, etaText: String?, progress: Float,
        addedAt: Long = currentTimeMillis(),
    ): Long {
        q.insertParcel(trackingNumber, carrier.name, itemName, status.name, etaText, progress.toDouble(), addedAt)
        return q.lastInsertRowId().executeAsOne()
    }

    suspend fun delete(id: Long) = q.deleteParcel(id)

    fun observeMonthlyCounts(): Flow<List<MonthlyCount>> =
        q.monthlyCounts().asFlow().mapToList(Dispatchers.Default).map { rows ->
            rows.map { MonthlyCount(month = it.month.orEmpty(), count = it.count) }
        }

    fun customsCode(): String? = q.getSetting(KEY_CUSTOMS).executeAsOneOrNull()

    suspend fun setCustomsCode(code: String) = q.putSetting(KEY_CUSTOMS, code)

    fun isOnboardingDone(): Boolean = q.getSetting(KEY_ONBOARDING_DONE).executeAsOneOrNull() == "true"

    suspend fun setOnboardingDone() = q.putSetting(KEY_ONBOARDING_DONE, "true")

    fun savedLang(): String? = q.getSetting(KEY_LANG).executeAsOneOrNull()

    suspend fun setLang(code: String) = q.putSetting(KEY_LANG, code)

    fun isDarkMode(): Boolean = q.getSetting(KEY_DARK).executeAsOneOrNull() == "true"

    suspend fun setDarkMode(dark: Boolean) = q.putSetting(KEY_DARK, if (dark) "true" else "false")
}
