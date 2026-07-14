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

    @Test fun explanation_for_jargon_term_in_english() {
        assertEquals(
            "The package has been loaded onto a truck for transport between regional logistics hubs.",
            StatusDictionary.explanation("간선상차", "en"),
        )
    }

    @Test fun explanation_for_jargon_term_in_korean() {
        assertEquals("물류 거점(터미널) 간 이동을 위해 화물차에 실린 단계입니다.", StatusDictionary.explanation("간선상차", "ko"))
    }

    @Test fun explanation_for_jargon_term_in_chinese() {
        assertEquals("包裹已装车，准备在地区物流枢纽之间运输。", StatusDictionary.explanation("간선상차", "zh"))
    }

    @Test fun explanation_for_jargon_term_in_vietnamese() {
        assertEquals(
            "Kiện hàng đã được xếp lên xe để vận chuyển giữa các trung tâm logistics khu vực.",
            StatusDictionary.explanation("간선상차", "vi"),
        )
    }

    @Test fun self_explanatory_term_has_no_explanation() {
        assertNull(StatusDictionary.explanation("배송완료", "en"))
    }

    @Test fun unknown_term_explanation_returns_null() {
        assertNull(StatusDictionary.explanation("존재하지않는용어", "en"))
    }
}
