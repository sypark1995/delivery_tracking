package com.parcelkr.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(dark: Boolean, content: @Composable () -> Unit) {
    val colors = if (dark) DarkColors else LightColors
    CompositionLocalProvider(
        LocalColors provides colors,
        LocalDimens provides AppDimens(),
    ) {
        MaterialTheme(typography = AppType.material, content = content)
    }
}
