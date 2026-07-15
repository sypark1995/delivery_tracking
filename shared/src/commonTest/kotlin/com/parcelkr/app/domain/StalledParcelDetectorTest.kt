package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.domain.model.Parcel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StalledParcelDetectorTest {
    private val dayMillis = 24L * 60 * 60 * 1000L

    private fun parcel(status: DeliveryStatus, addedAt: Long) = Parcel(
        id = 1, trackingNumber = "T1", carrier = Carrier.CJ, itemName = "Item",
        status = status, etaText = null, progress = 0.5f, addedAt = addedAt,
    )

    @Test fun not_stalled_when_younger_than_threshold() {
        val now = STALLED_THRESHOLD_DAYS * dayMillis
        val p = parcel(DeliveryStatus.IN_TRANSIT, addedAt = now - (STALLED_THRESHOLD_DAYS - 1) * dayMillis)
        assertFalse(isStalled(p, now))
    }

    @Test fun stalled_when_at_or_past_threshold() {
        val now = 10 * STALLED_THRESHOLD_DAYS * dayMillis
        val p = parcel(DeliveryStatus.IN_TRANSIT, addedAt = now - STALLED_THRESHOLD_DAYS * dayMillis)
        assertTrue(isStalled(p, now))
    }

    @Test fun never_stalled_when_delivered() {
        val now = 100 * dayMillis
        val p = parcel(DeliveryStatus.DELIVERED, addedAt = 0L)
        assertTrue(daysSinceAdded(p, now) >= STALLED_THRESHOLD_DAYS)
        assertFalse(isStalled(p, now))
    }

    @Test fun days_since_added_computes_whole_days() {
        val p = parcel(DeliveryStatus.IN_TRANSIT, addedAt = 0L)
        assertEquals(3, daysSinceAdded(p, now = 3 * dayMillis + 1000))
    }
}
