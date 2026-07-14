package com.parcelkr.app.data.real

import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private const val CJ_DELIVERED_FIXTURE = """
{"data": {"track": {"trackingNumber": "000000000000", "lastEvent": {"status": {"code": "DELIVERED", "name": "배송완료"}, "time": "2026-07-02T15:11:06.000+09:00", "description": "고객님의 상품이 배송완료 되었습니다.(담당사원:권진홍 010-4742-7273)", "contact": {"name": "권진홍", "phoneNumber": "+821047427273"}}, "events": {"edges": [{"node": {"status": {"code": "AT_PICKUP", "name": "집화처리"}, "time": "2026-06-18T09:15:31.000+09:00", "description": "보내시는 고객님으로부터 상품을 인수받았습니다", "location": {"name": "서울마포홍익"}, "contact": null}}, {"node": {"status": {"code": "DELIVERED", "name": "배송완료"}, "time": "2026-07-02T15:11:06.000+09:00", "description": "고객님의 상품이 배송완료 되었습니다.(담당사원:권진홍 010-4742-7273)", "location": {"name": "경기의정부민락"}, "contact": {"name": "권진홍", "phoneNumber": "+821047427273"}}}]}}}}
"""

class TrackInfoMapperTest {

    @Test fun maps_real_cj_delivered_response() {
        val response = Json.decodeFromString<TrackGraphQLResponse>(CJ_DELIVERED_FIXTURE)
        val result = TrackInfoMapper.toTrackingResult(response.data!!.track!!, Carrier.CJ)

        assertEquals("000000000000", result.trackingNumber)
        assertEquals(Carrier.CJ, result.carrier)
        assertEquals(DeliveryStatus.DELIVERED, result.status)
        assertEquals(1.0f, result.progress)
        assertEquals("권진홍", result.driverName)
        assertEquals("+821047427273", result.driverPhone)
        assertEquals(2, result.events.size)

        assertEquals(DeliveryStatus.DELIVERED, result.events[0].status)
        assertEquals("배송완료", result.events[0].labelKo)
        assertEquals("경기의정부민락", result.events[0].place)
        assertEquals("2026-07-02 15:11", result.events[0].timeText)

        assertEquals(DeliveryStatus.RECEIVED, result.events[1].status)
        assertEquals("집화처리", result.events[1].labelKo)
        assertEquals("서울마포홍익", result.events[1].place)
        assertEquals("2026-06-18 09:15", result.events[1].timeText)
    }

    @Test fun statusFor_maps_every_known_code() {
        assertEquals(DeliveryStatus.RECEIVED, TrackInfoMapper.statusFor("UNKNOWN"))
        assertEquals(DeliveryStatus.RECEIVED, TrackInfoMapper.statusFor("INFORMATION_RECEIVED"))
        assertEquals(DeliveryStatus.RECEIVED, TrackInfoMapper.statusFor("AT_PICKUP"))
        assertEquals(DeliveryStatus.IN_TRANSIT, TrackInfoMapper.statusFor("IN_TRANSIT"))
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, TrackInfoMapper.statusFor("OUT_FOR_DELIVERY"))
        assertEquals(DeliveryStatus.FAILED, TrackInfoMapper.statusFor("ATTEMPT_FAIL"))
        assertEquals(DeliveryStatus.FAILED, TrackInfoMapper.statusFor("EXCEPTION"))
        assertEquals(DeliveryStatus.DELIVERED, TrackInfoMapper.statusFor("DELIVERED"))
        assertEquals(DeliveryStatus.DELAYED, TrackInfoMapper.statusFor("AVAILABLE_FOR_PICKUP"))
    }

    @Test fun carrierIds_map_to_confirmed_real_ids() {
        assertEquals("kr.cjlogistics", CarrierIds.idFor(Carrier.CJ))
        assertEquals("kr.epost", CarrierIds.idFor(Carrier.KOREA_POST))
        assertEquals("kr.coupangls", CarrierIds.idFor(Carrier.COUPANG))
        assertEquals("kr.lotte", CarrierIds.idFor(Carrier.LOTTE))
        assertEquals("kr.hanjin", CarrierIds.idFor(Carrier.HANJIN))
        assertFailsWith<IllegalArgumentException> { CarrierIds.idFor(Carrier.UNKNOWN) }
    }
}
