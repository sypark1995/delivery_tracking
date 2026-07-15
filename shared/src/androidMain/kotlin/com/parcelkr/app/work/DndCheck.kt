package com.parcelkr.app.work

import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.domain.isWithinDnd
import java.time.LocalTime

fun ParcelRepository.isCurrentlyDnd(): Boolean {
    if (!dndEnabled()) return false
    val now = LocalTime.now()
    return isWithinDnd(now.hour * 60 + now.minute, dndStartMinute(), dndEndMinute())
}
