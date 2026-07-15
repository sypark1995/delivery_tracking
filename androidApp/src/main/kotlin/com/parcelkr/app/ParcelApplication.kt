package com.parcelkr.app

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.parcelkr.app.di.initKoin
import com.parcelkr.app.work.DAILY_DIGEST_WORK_NAME
import com.parcelkr.app.work.DailyDigestWorker
import com.parcelkr.app.work.PARCEL_REFRESH_WORK_NAME
import com.parcelkr.app.work.ParcelRefreshWorker
import com.parcelkr.app.work.createParcelRefreshNotificationChannel
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class ParcelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this, BuildConfig.TRACKER_CLIENT_ID, BuildConfig.TRACKER_CLIENT_SECRET)
        createParcelRefreshNotificationChannel(this)
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            PARCEL_REFRESH_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<ParcelRefreshWorker>(15, TimeUnit.MINUTES).build(),
        )
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DAILY_DIGEST_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<DailyDigestWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(millisUntilNextNineAm(), TimeUnit.MILLISECONDS)
                .build(),
        )
    }

    private fun millisUntilNextNineAm(): Long {
        val now = LocalDateTime.now()
        var next = now.withHour(9).withMinute(0).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusDays(1)
        return Duration.between(now, next).toMillis()
    }
}
