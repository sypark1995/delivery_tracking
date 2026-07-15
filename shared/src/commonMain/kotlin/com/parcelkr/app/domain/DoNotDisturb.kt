package com.parcelkr.app.domain

fun isWithinDnd(nowMinuteOfDay: Int, startMinute: Int, endMinute: Int): Boolean =
    if (startMinute < endMinute) {
        nowMinuteOfDay in startMinute until endMinute
    } else if (startMinute > endMinute) {
        nowMinuteOfDay >= startMinute || nowMinuteOfDay < endMinute
    } else {
        false
    }
