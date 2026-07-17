package com.parcelkr.app.ui.components

import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.i18n.Lang
import com.parcelkr.app.i18n.stringsFor
import kotlin.test.Test
import kotlin.test.assertEquals

class StatusVisualTest {
    @Test fun status_label_is_localized_for_every_status_in_korean() {
        val ko = stringsFor(Lang.KO)
        assertEquals(ko.statusReceived, statusLabel(DeliveryStatus.RECEIVED, ko))
        assertEquals(ko.statusInTransit, statusLabel(DeliveryStatus.IN_TRANSIT, ko))
        assertEquals(ko.statusOutForDelivery, statusLabel(DeliveryStatus.OUT_FOR_DELIVERY, ko))
        assertEquals(ko.delivered, statusLabel(DeliveryStatus.DELIVERED, ko))
        assertEquals(ko.statusDeliveryFailed, statusLabel(DeliveryStatus.FAILED, ko))
        assertEquals(ko.statusDelayed, statusLabel(DeliveryStatus.DELAYED, ko))
    }

    @Test fun status_label_differs_between_english_and_korean_for_non_delivered_statuses() {
        val en = stringsFor(Lang.EN)
        val ko = stringsFor(Lang.KO)
        DeliveryStatus.entries.filter { it != DeliveryStatus.DELIVERED }.forEach { status ->
            assert(statusLabel(status, en) != statusLabel(status, ko)) {
                "expected English/Korean labels to differ for $status"
            }
        }
    }
}
