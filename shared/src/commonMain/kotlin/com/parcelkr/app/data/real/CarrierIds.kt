package com.parcelkr.app.data.real

import com.parcelkr.app.domain.model.Carrier

object CarrierIds {
    fun idFor(carrier: Carrier): String = when (carrier) {
        Carrier.CJ -> "kr.cjlogistics"
        Carrier.KOREA_POST -> "kr.epost"
        Carrier.COUPANG -> "kr.coupangls"
        Carrier.LOTTE -> "kr.lotte"
        Carrier.HANJIN -> "kr.hanjin"
        Carrier.UNKNOWN -> throw IllegalArgumentException("Cannot track UNKNOWN carrier via Delivery Tracker")
    }
}
