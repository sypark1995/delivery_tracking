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
}
