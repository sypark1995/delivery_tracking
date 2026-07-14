package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.DeliveryStatus

object StatusDictionary {
    private data class Term(
        val status: DeliveryStatus,
        val en: String, val ko: String, val zh: String, val vi: String,
        val explainEn: String? = null, val explainKo: String? = null,
        val explainZh: String? = null, val explainVi: String? = null,
    )

    // Korean logistics term -> normalized status + per-language label.
    private val terms: Map<String, Term> = mapOf(
        "집화처리" to Term(
            DeliveryStatus.RECEIVED, "Picked up", "집화처리", "已揽收", "Đã lấy hàng",
            explainEn = "The courier has picked up the package from the sender.",
            explainKo = "택배기사가 발송인으로부터 물건을 받아간 단계입니다.",
            explainZh = "快递员已从寄件人处取件。",
            explainVi = "Người giao hàng đã lấy kiện hàng từ người gửi.",
        ),
        "접수" to Term(DeliveryStatus.RECEIVED, "Received", "접수", "已受理", "Đã tiếp nhận"),
        "간선상차" to Term(
            DeliveryStatus.IN_TRANSIT, "In transit", "간선상차", "运输中", "Đang vận chuyển",
            explainEn = "The package has been loaded onto a truck for transport between regional logistics hubs.",
            explainKo = "물류 거점(터미널) 간 이동을 위해 화물차에 실린 단계입니다.",
            explainZh = "包裹已装车，准备在地区物流枢纽之间运输。",
            explainVi = "Kiện hàng đã được xếp lên xe để vận chuyển giữa các trung tâm logistics khu vực.",
        ),
        "간선하차" to Term(
            DeliveryStatus.IN_TRANSIT, "Arrived at hub", "간선하차", "到达分拣中心", "Đến trung tâm",
            explainEn = "The truck has arrived at a regional logistics hub and unloaded the package.",
            explainKo = "화물차가 물류 거점(터미널)에 도착해 짐을 내린 단계입니다.",
            explainZh = "货车已到达地区物流枢纽并卸货。",
            explainVi = "Xe tải đã đến trung tâm logistics khu vực và dỡ hàng.",
        ),
        "배송출발" to Term(DeliveryStatus.OUT_FOR_DELIVERY, "Out for delivery", "배송출발", "派送中", "Đang giao hàng"),
        "배송완료" to Term(DeliveryStatus.DELIVERED, "Delivered", "배송완료", "已送达", "Đã giao"),
        "미배송" to Term(DeliveryStatus.FAILED, "Delivery failed", "미배송", "投递失败", "Giao không thành công"),
        "통관진행중" to Term(
            DeliveryStatus.IN_TRANSIT, "Customs clearance in progress", "통관진행중", "清关中", "Đang thông quan",
            explainEn = "The package is undergoing customs inspection after arriving from overseas.",
            explainKo = "해외에서 온 물건이 세관 검사를 받고 있는 단계입니다.",
            explainZh = "包裹正在接受海外抵达后的海关检查。",
            explainVi = "Kiện hàng đang được hải quan kiểm tra sau khi đến từ nước ngoài.",
        ),
    )

    fun translate(koTerm: String, langCode: String): String? {
        val t = terms[koTerm] ?: return null
        return when (langCode) {
            "en" -> t.en; "ko" -> t.ko; "zh" -> t.zh; "vi" -> t.vi
            else -> t.en
        }
    }

    fun statusFor(koTerm: String): DeliveryStatus? = terms[koTerm]?.status

    fun explanation(koTerm: String, langCode: String): String? {
        val t = terms[koTerm] ?: return null
        return when (langCode) {
            "en" -> t.explainEn; "ko" -> t.explainKo; "zh" -> t.explainZh; "vi" -> t.explainVi
            else -> t.explainEn
        }
    }
}
