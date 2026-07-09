package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.DeliveryStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StatusDictionaryTest {
    @Test fun translates_known_term_to_english() {
        assertEquals("Out for delivery", StatusDictionary.translate("배송출발", "en"))
    }
    @Test fun translates_known_term_to_vietnamese() {
        assertEquals("Đang giao hàng", StatusDictionary.translate("배송출발", "vi"))
    }
    @Test fun unknown_term_returns_null() {
        assertNull(StatusDictionary.translate("우주선발사", "en"))
    }
    @Test fun maps_term_to_status() {
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, StatusDictionary.statusFor("배송출발"))
        assertEquals(DeliveryStatus.RECEIVED, StatusDictionary.statusFor("집화처리"))
    }
}
