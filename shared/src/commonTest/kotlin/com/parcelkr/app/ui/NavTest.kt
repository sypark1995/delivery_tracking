package com.parcelkr.app.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NavTest {
    @Test fun home_and_onboarding_have_no_back_target() {
        assertNull(backTargetFor(Screen.Home))
        assertNull(backTargetFor(Screen.Onboarding))
    }

    @Test fun history_goes_back_to_settings() {
        assertEquals(Screen.Settings, backTargetFor(Screen.History))
    }

    @Test fun forwarding_goes_back_to_settings() {
        assertEquals(Screen.Settings, backTargetFor(Screen.Forwarding))
    }

    @Test fun forwarding_add_and_detail_go_back_to_forwarding_list() {
        assertEquals(Screen.Forwarding, backTargetFor(Screen.ForwardingAdd))
        assertEquals(Screen.Forwarding, backTargetFor(Screen.ForwardingDetail(1L)))
    }

    @Test fun other_screens_go_back_to_home() {
        assertEquals(Screen.Home, backTargetFor(Screen.Add))
        assertEquals(Screen.Home, backTargetFor(Screen.Settings))
        assertEquals(Screen.Home, backTargetFor(Screen.Updates))
        assertEquals(Screen.Home, backTargetFor(Screen.Detail("123", "CJ")))
        assertEquals(Screen.Home, backTargetFor(Screen.Contact("123", "CJ")))
    }
}
