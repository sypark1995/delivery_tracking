package com.parcelkr.app.ui

sealed interface Screen {
    data object Onboarding : Screen
    data object Home : Screen
    data object Add : Screen
    data class Detail(val trackingNumber: String, val carrierName: String) : Screen
    data class Contact(val trackingNumber: String, val carrierName: String) : Screen
    data object Settings : Screen
    data object Updates : Screen
    data object History : Screen
    data object Forwarding : Screen
    data object ForwardingAdd : Screen
    data class ForwardingDetail(val id: Long) : Screen
}

// Mirrors each screen's own onBack wiring in ParcelApp.kt, so the hardware/gesture back button
// navigates the same in-app screen stack instead of exiting the app entirely.
fun backTargetFor(screen: Screen): Screen? = when (screen) {
    Screen.Home, Screen.Onboarding -> null
    Screen.History -> Screen.Settings
    Screen.ForwardingAdd, is Screen.ForwardingDetail -> Screen.Forwarding
    Screen.Forwarding -> Screen.Settings
    else -> Screen.Home
}
