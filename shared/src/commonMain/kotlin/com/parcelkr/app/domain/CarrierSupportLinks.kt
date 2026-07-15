package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.Carrier

/**
 * Official tracking/customer-service pages per carrier. There is no consistent
 * "redelivery request" deep link scheme across Korean couriers, so these point to
 * each carrier's own site where the user can find the right option (redelivery,
 * pickup-locker info, or support chat) — not a fully automated booking flow.
 */
object CarrierSupportLinks {
    fun urlFor(carrier: Carrier): String = when (carrier) {
        Carrier.CJ -> "https://www.cjlogistics.com/ko/tool/parcel/tracking"
        Carrier.KOREA_POST -> "https://service.epost.go.kr/iservice/usr/trace/usrtrc001k01.jsp"
        Carrier.COUPANG -> "https://helpcenter.coupangcorp.com/hc/ko/requests/new"
        Carrier.LOTTE -> "https://www.lotteglogis.com/home/reservation/tracking/index"
        Carrier.HANJIN -> "https://www.hanjin.com"
        Carrier.UNKNOWN -> "https://www.google.com/search?q=%ED%83%9D%EB%B0%B0+%EC%9E%AC%EB%B0%B0%EC%86%A1+%EC%8B%A0%EC%B2%AD"
    }
}
