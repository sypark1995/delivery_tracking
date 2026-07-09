package com.parcelkr.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object AppType {
    val display = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium, lineHeight = 26.sp)
    val title = TextStyle(fontSize = 21.sp, fontWeight = FontWeight.Medium, lineHeight = 26.sp)
    val body = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 26.sp)
    val label = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, lineHeight = 18.sp)
    val caption = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Normal, lineHeight = 16.sp)
    val material = Typography()
}
