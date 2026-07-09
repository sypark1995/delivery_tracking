package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.DeliveryStatus

object StatusDictionary {
    private data class Term(
        val status: DeliveryStatus,
        val en: String, val ko: String, val zh: String, val vi: String,
    )

    // Korean logistics term -> normalized status + per-language label.
    private val terms: Map<String, Term> = mapOf(
        "집화처리" to Term(DeliveryStatus.RECEIVED, "Picked up", "집화처리", "已揽收", "Đã lấy hàng"),
        "접수" to Term(DeliveryStatus.RECEIVED, "Received", "접수", "已受理", "Đã tiếp nhận"),
        "간선상차" to Term(DeliveryStatus.IN_TRANSIT, "In transit", "간선상차", "运输中", "Đang vận chuyển"),
        "간선하차" to Term(DeliveryStatus.IN_TRANSIT, "Arrived at hub", "간선하차", "到达分拣中心", "Đến trung tâm"),
        "배송출발" to Term(DeliveryStatus.OUT_FOR_DELIVERY, "Out for delivery", "배송출발", "派送中", "Đang giao hàng"),
        "배송완료" to Term(DeliveryStatus.DELIVERED, "Delivered", "배송완료", "已送达", "Đã giao"),
        "미배송" to Term(DeliveryStatus.FAILED, "Delivery failed", "미배송", "投递失败", "Giao không thành công"),
        "통관진행중" to Term(DeliveryStatus.IN_TRANSIT, "Customs clearance in progress", "통관진행중", "清关中", "Đang thông quan"),
    )

    fun translate(koTerm: String, langCode: String): String? {
        val t = terms[koTerm] ?: return null
        return when (langCode) {
            "en" -> t.en; "ko" -> t.ko; "zh" -> t.zh; "vi" -> t.vi
            else -> t.en
        }
    }

    fun statusFor(koTerm: String): DeliveryStatus? = terms[koTerm]?.status
}
