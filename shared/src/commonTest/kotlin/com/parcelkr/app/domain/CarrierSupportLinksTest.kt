package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.Carrier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CarrierSupportLinksTest {
    @Test fun returns_official_url_for_each_known_carrier() {
        assertEquals("https://www.cjlogistics.com/ko/tool/parcel/tracking", CarrierSupportLinks.urlFor(Carrier.CJ))
        assertEquals("https://service.epost.go.kr/iservice/usr/trace/usrtrc001k01.jsp", CarrierSupportLinks.urlFor(Carrier.KOREA_POST))
        assertEquals("https://helpcenter.coupangcorp.com/hc/ko/requests/new", CarrierSupportLinks.urlFor(Carrier.COUPANG))
        assertEquals("https://www.lotteglogis.com/home/reservation/tracking/index", CarrierSupportLinks.urlFor(Carrier.LOTTE))
        assertEquals("https://www.hanjin.com", CarrierSupportLinks.urlFor(Carrier.HANJIN))
    }

    @Test fun unknown_carrier_falls_back_to_a_non_blank_generic_url() {
        assertTrue(CarrierSupportLinks.urlFor(Carrier.UNKNOWN).startsWith("https://"))
    }
}
