package com.parcelkr.app.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DoNotDisturbTest {
    @Test fun same_day_window_includes_start_excludes_end() {
        // 22:00 (1320) start is inclusive, 23:30 (1410) end is exclusive within a same-day window
        assertTrue(isWithinDnd(nowMinuteOfDay = 21 * 60 + 30, startMinute = 21 * 60, endMinute = 23 * 60))
        assertFalse(isWithinDnd(nowMinuteOfDay = 23 * 60, startMinute = 21 * 60, endMinute = 23 * 60))
    }

    @Test fun same_day_window_excludes_before_start_and_after_end() {
        assertFalse(isWithinDnd(nowMinuteOfDay = 20 * 60, startMinute = 21 * 60, endMinute = 23 * 60))
        assertFalse(isWithinDnd(nowMinuteOfDay = 23 * 60 + 1, startMinute = 21 * 60, endMinute = 23 * 60))
    }

    @Test fun overnight_window_wraps_past_midnight() {
        // 22:00 (1320) to 08:00 (480) — active late at night AND early morning
        assertTrue(isWithinDnd(nowMinuteOfDay = 23 * 60, startMinute = 22 * 60, endMinute = 8 * 60))
        assertTrue(isWithinDnd(nowMinuteOfDay = 0, startMinute = 22 * 60, endMinute = 8 * 60))
        assertTrue(isWithinDnd(nowMinuteOfDay = 7 * 60 + 59, startMinute = 22 * 60, endMinute = 8 * 60))
    }

    @Test fun overnight_window_excludes_daytime() {
        assertFalse(isWithinDnd(nowMinuteOfDay = 8 * 60, startMinute = 22 * 60, endMinute = 8 * 60))
        assertFalse(isWithinDnd(nowMinuteOfDay = 12 * 60, startMinute = 22 * 60, endMinute = 8 * 60))
        assertFalse(isWithinDnd(nowMinuteOfDay = 21 * 60 + 59, startMinute = 22 * 60, endMinute = 8 * 60))
    }

    @Test fun equal_start_and_end_means_never_active() {
        assertFalse(isWithinDnd(nowMinuteOfDay = 22 * 60, startMinute = 22 * 60, endMinute = 22 * 60))
        assertFalse(isWithinDnd(nowMinuteOfDay = 0, startMinute = 22 * 60, endMinute = 22 * 60))
    }
}
