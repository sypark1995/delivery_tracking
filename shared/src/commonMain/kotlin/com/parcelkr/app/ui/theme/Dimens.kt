package com.parcelkr.app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimens(
    val xs: Dp = 4.dp, val s: Dp = 8.dp, val m: Dp = 12.dp,
    val l: Dp = 16.dp, val xl: Dp = 24.dp, val xxl: Dp = 32.dp,
    val screenH: Dp = 16.dp, val cardPad: Dp = 15.dp, val minTap: Dp = 48.dp,
)
val LocalDimens = staticCompositionLocalOf { AppDimens() }
