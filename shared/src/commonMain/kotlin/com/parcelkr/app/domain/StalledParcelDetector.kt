package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel

const val STALLED_THRESHOLD_DAYS = 3L
private const val DAY_MILLIS = 24L * 60 * 60 * 1000L

fun daysSinceAdded(parcel: Parcel, now: Long): Long = (now - parcel.addedAt) / DAY_MILLIS

fun isStalled(parcel: Parcel, now: Long): Boolean =
    parcel.status != DeliveryStatus.DELIVERED && daysSinceAdded(parcel, now) >= STALLED_THRESHOLD_DAYS
