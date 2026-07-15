package com.parcelkr.app.work

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.deviceLang
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.i18n.Lang
import com.parcelkr.app.i18n.stringsFor
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val DAILY_DIGEST_WORK_NAME = "daily_delivery_digest"
private const val DAILY_DIGEST_NOTIFICATION_ID = 1_000_000

class DailyDigestWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {
    private val repo: ParcelRepository by inject()

    override suspend fun doWork(): Result {
        if (!repo.notificationsEnabled()) return Result.success()
        val activeCount = repo.observeParcels().first().count { it.status != DeliveryStatus.DELIVERED }
        if (activeCount == 0) return Result.success()

        val lang = repo.savedLang()?.let { Lang.fromCode(it) } ?: deviceLang()
        val strings = stringsFor(lang)
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification.Builder(applicationContext, PARCEL_REFRESH_CHANNEL_ID)
            .setContentTitle(strings.dailyDigestTitle)
            .setContentText(strings.dailyDigestBody.replace("{count}", activeCount.toString()))
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setAutoCancel(true)
            .build()
        manager.notify(DAILY_DIGEST_NOTIFICATION_ID, notification)
        return Result.success()
    }
}
