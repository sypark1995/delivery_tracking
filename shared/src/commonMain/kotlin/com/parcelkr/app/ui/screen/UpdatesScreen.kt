package com.parcelkr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.ui.components.SectionHeader
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun UpdatesScreen(onBack: () -> Unit) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    Column(Modifier.fillMaxSize().background(colors.bg)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = colors.textPrimary,
                modifier = Modifier.clickable { onBack() })
            Spacer(Modifier.size(10.dp))
            Text(strings.updates, style = AppType.title, color = colors.textPrimary)
        }
        Row(
            Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                .clip(AppShapes.card).background(colors.surface)
                .border(1.dp, colors.border, AppShapes.card).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.LocalShipping, contentDescription = null, tint = colors.brand,
                modifier = Modifier.clip(AppShapes.field).background(colors.brandTint).padding(6.dp).size(18.dp))
            Spacer(Modifier.size(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Out for delivery", style = AppType.label, color = colors.textPrimary)
                Text("Nike Air Max is on its way — arriving 2–4 PM today.", style = AppType.caption, color = colors.textSecondary)
            }
            Text("now", style = AppType.caption, color = colors.textMuted)
        }
        SectionHeader(strings.earlier)
        UpdateRow(Color(0xFF2E5AAC), "Documents · Korea Post", "Customs clearance in progress")
        UpdateRow(Color(0xFF0E8F6E), "Groceries · Coupang", "Delivered — left at the door")
        UpdateRow(Color(0xFF6B7280), "Nike Air Max · CJ", "Picked up in Busan")
    }
}

@Composable
private fun UpdateRow(dot: Color, title: String, subtitle: String) {
    val colors = LocalColors.current
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
        Spacer(Modifier.size(8.dp).clip(CircleShape).background(dot))
        Spacer(Modifier.size(10.dp))
        Column {
            Text(title, style = AppType.label, color = colors.textPrimary)
            Text(subtitle, style = AppType.caption, color = colors.textSecondary)
        }
    }
}
