package com.parcelkr.app.data.real

import com.parcelkr.app.domain.model.DeliveryStatus
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

private const val IN_TRANSIT_FIXTURE = """
{
  "code": 0,
  "data": {
    "accepted": [
      {
        "number": "RB123456789CN",
        "track_info": {
          "latest_status": { "status": "InTransit" },
          "tracking": {
            "providers": [
              {
                "provider": { "name": "China Post" },
                "events": [
                  { "time_iso": "2026-07-09T14:20:00+08:00", "description": "Departed from origin country", "location": "Shenzhen, China", "stage": "InTransit" },
                  { "time_iso": "2026-07-08T09:00:00+08:00", "description": "Order information received", "location": null, "stage": "InfoReceived" }
                ]
              }
            ]
          }
        }
      }
    ]
  }
}
"""

class Track17MapperTest {
    @Test fun maps_in_transit_response() {
        val response = Json.decodeFromString<Track17GetTrackInfoResponse>(IN_TRANSIT_FIXTURE)
        val result = Track17Mapper.toOverseasResult(response.data!!.accepted[0])

        assertEquals("RB123456789CN", result.trackingNumber)
        assertEquals("China Post", result.carrierName)
        assertEquals(DeliveryStatus.IN_TRANSIT, result.status)
        assertEquals(2, result.events.size)

        assertEquals(DeliveryStatus.IN_TRANSIT, result.events[0].status)
        assertEquals("Departed from origin country", result.events[0].description)
        assertEquals("Shenzhen, China", result.events[0].location)
        assertEquals("2026-07-09 14:20", result.events[0].timeText)

        assertEquals(DeliveryStatus.RECEIVED, result.events[1].status)
        assertEquals("Order information received", result.events[1].description)
        assertEquals(null, result.events[1].location)
    }

    @Test fun statusFor_maps_every_known_stage() {
        assertEquals(DeliveryStatus.RECEIVED, Track17Mapper.statusFor("NotFound"))
        assertEquals(DeliveryStatus.RECEIVED, Track17Mapper.statusFor("InfoReceived"))
        assertEquals(DeliveryStatus.IN_TRANSIT, Track17Mapper.statusFor("InTransit"))
        assertEquals(DeliveryStatus.DELAYED, Track17Mapper.statusFor("Expired"))
        assertEquals(DeliveryStatus.DELAYED, Track17Mapper.statusFor("AvailableForPickup"))
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, Track17Mapper.statusFor("OutForDelivery"))
        assertEquals(DeliveryStatus.FAILED, Track17Mapper.statusFor("DeliveryFailure"))
        assertEquals(DeliveryStatus.DELIVERED, Track17Mapper.statusFor("Delivered"))
        assertEquals(DeliveryStatus.FAILED, Track17Mapper.statusFor("Exception"))
        assertEquals(DeliveryStatus.RECEIVED, Track17Mapper.statusFor(null))
    }

    @Test fun missing_track_info_maps_to_empty_events_and_received_status() {
        val response = Json.decodeFromString<Track17GetTrackInfoResponse>(
            """{"code":0,"data":{"accepted":[{"number":"XYZ"}]}}""",
        )
        val result = Track17Mapper.toOverseasResult(response.data!!.accepted[0])

        assertEquals("XYZ", result.trackingNumber)
        assertEquals(null, result.carrierName)
        assertEquals(DeliveryStatus.RECEIVED, result.status)
        assertEquals(emptyList(), result.events)
    }
}
