package com.parcelkr.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType

@Composable
fun StalledBadge(days: Long, modifier: Modifier = Modifier) {
    val strings = LocalStrings.current
    val c = statusColorsFor(DeliveryStatus.DELAYED)
    Row(
        modifier = modifier
            .clip(AppShapes.pill)
            .background(c.tintBg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Outlined.Warning, contentDescription = null, tint = c.tintText, modifier = Modifier.size(14.dp))
        Text(strings.stalledBadge.replace("{days}", days.toString()), style = AppType.label, color = c.tintText)
    }
}
