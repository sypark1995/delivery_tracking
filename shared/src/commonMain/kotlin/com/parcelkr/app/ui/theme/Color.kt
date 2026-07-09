package com.parcelkr.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val bg: Color,
    val surface: Color,
    val border: Color,
    val borderStrong: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val segmentTrack: Color,
    val brand: Color,
    val brandPressed: Color,
    val brandTint: Color,
    val onTint: Color,
)

val LightColors = AppColors(
    bg = Color(0xFFF4F2ED),
    surface = Color(0xFFFFFFFF),
    border = Color(0xFFE9E5DC),
    borderStrong = Color(0xFFDCD7CC),
    textPrimary = Color(0xFF17130E),
    textSecondary = Color(0xFF8A8375),
    textMuted = Color(0xFFB4AD9E),
    segmentTrack = Color(0xFFEAE7E0),
    brand = Color(0xFF0E8F6E),
    brandPressed = Color(0xFF0B7458),
    brandTint = Color(0xFFDCF3E9),
    onTint = Color(0xFF0B5C46),
)

val DarkColors = AppColors(
    bg = Color(0xFF16130F),
    surface = Color(0xFF211D18),
    border = Color(0xFF332E27),
    borderStrong = Color(0xFF3E3830),
    textPrimary = Color(0xFFF0ECE4),
    textSecondary = Color(0xFFA8A192),
    textMuted = Color(0xFF837B6C),
    segmentTrack = Color(0xFF2A251F),
    brand = Color(0xFF2FB98F),
    brandPressed = Color(0xFF259C78),
    brandTint = Color(0xFF163A2E),
    onTint = Color(0xFF7FD8B9),
)

val LocalColors = staticCompositionLocalOf { LightColors }

@Immutable
data class StatusColors(val dot: Color, val tintBg: Color, val tintText: Color)

// Keyed by DeliveryStatus.name.
val StatusPalette: Map<String, StatusColors> = mapOf(
    "RECEIVED" to StatusColors(Color(0xFF6B7280), Color(0xFFEFEDE7), Color(0xFF4B4A45)),
    "IN_TRANSIT" to StatusColors(Color(0xFF2E5AAC), Color(0xFFE3ECFA), Color(0xFF1E3E7A)),
    "OUT_FOR_DELIVERY" to StatusColors(Color(0xFFD9822B), Color(0xFFFBECD9), Color(0xFF8A4A12)),
    "DELIVERED" to StatusColors(Color(0xFF0E8F6E), Color(0xFFDCF3E9), Color(0xFF0B5C46)),
    "FAILED" to StatusColors(Color(0xFFC6483C), Color(0xFFFBE6E3), Color(0xFF8A2A22)),
    "DELAYED" to StatusColors(Color(0xFFB25A00), Color(0xFFFBEBD6), Color(0xFF7A3D00)),
)
