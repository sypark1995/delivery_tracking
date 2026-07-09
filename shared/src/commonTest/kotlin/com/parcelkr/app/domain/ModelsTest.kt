package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.DeliveryStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelsTest {
    @Test fun carrier_has_display_name() {
        assertEquals("CJ Logistics", Carrier.CJ.displayName)
    }
    @Test fun delivery_status_has_six_values() {
        assertEquals(6, DeliveryStatus.entries.size)
    }
}
