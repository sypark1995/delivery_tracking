package com.parcelkr.app.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.parcelkr.app.data.ForwardingParcelRepository
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.deviceLang
import com.parcelkr.app.domain.ForwardingRefresher
import com.parcelkr.app.domain.ForwardingStatusChange
import com.parcelkr.app.domain.OverseasTrackingApi
import com.parcelkr.app.domain.ParcelRefresher
import com.parcelkr.app.domain.StatusChange
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.i18n.Lang
import com.parcelkr.app.i18n.stringsFor
import com.parcelkr.app.ui.components.statusLabel
import com.parcelkr.app.widget.ParcelWidget
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val PARCEL_REFRESH_CHANNEL_ID = "parcel_status_updates"
const val PARCEL_REFRESH_WORK_NAME = "parcel_status_refresh"

class ParcelRefreshWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {
    private val repo: ParcelRepository by inject()
    private val api: TrackingApi by inject()
    private val forwardingRepo: ForwardingParcelRepository by inject()
    private val overseasApi: OverseasTrackingApi by inject()

    override suspend fun doWork(): Result {
        val hasWidget = GlanceAppWidgetManager(applicationContext).getGlanceIds(ParcelWidget::class.java).isNotEmpty()
        val notificationsOn = repo.notificationsEnabled()
        if (!notificationsOn && !hasWidget) return Result.success()

        val changes = try {
            ParcelRefresher(repo, api).refresh()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return Result.retry()
        }

        if (notificationsOn && !repo.isCurrentlyDnd()) {
            changes.forEach { notify(it) }
        }
        if (hasWidget) {
            ParcelWidget().updateAll(applicationContext)
        }

        // Forwarding (overseas) parcels have no widget surface, so they're only worth refreshing
        // when there's a notification to show for them.
        if (notificationsOn) {
            val forwardingChanges = try {
                ForwardingRefresher(forwardingRepo, overseasApi).refresh()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                emptyList()
            }
            if (!repo.isCurrentlyDnd()) {
                forwardingChanges.forEach { notifyForwarding(it) }
            }
        }

        return Result.success()
    }

    private fun notify(change: StatusChange) {
        val lang = repo.savedLang()?.let { Lang.fromCode(it) } ?: deviceLang()
        val strings = stringsFor(lang)
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification.Builder(applicationContext, PARCEL_REFRESH_CHANNEL_ID)
            .setContentTitle(strings.statusChangeNotificationTitle)
            .setContentText("${change.parcel.itemName} · ${change.parcel.carrier.displayName} — ${statusLabel(change.newStatus, strings)}")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setAutoCancel(true)
            .build()
        manager.notify(change.parcel.id.toInt(), notification)
    }

    private fun notifyForwarding(change: ForwardingStatusChange) {
        val lang = repo.savedLang()?.let { Lang.fromCode(it) } ?: deviceLang()
        val strings = stringsFor(lang)
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification.Builder(applicationContext, PARCEL_REFRESH_CHANNEL_ID)
            .setContentTitle(strings.statusChangeNotificationTitle)
            .setContentText("${change.parcel.itemName} · ${change.parcel.overseasCarrierName ?: strings.overseasSection} — ${statusLabel(change.newStatus, strings)}")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setAutoCancel(true)
            .build()
        // Forwarding parcel IDs share the same notification-id space as regular parcels; offset
        // well clear of any plausible regular-parcel id range to avoid collisions.
        manager.notify((1_000_000_000L + change.parcel.id).toInt(), notification)
    }
}

fun createParcelRefreshNotificationChannel(context: Context) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channel = NotificationChannel(
        PARCEL_REFRESH_CHANNEL_ID,
        "Delivery status updates",
        NotificationManager.IMPORTANCE_DEFAULT,
    )
    manager.createNotificationChannel(channel)
}
