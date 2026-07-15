package com.parcelkr.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
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
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun ParcelCard(
    itemName: String,
    carrierName: String,
    status: DeliveryStatus,
    stalledDays: Long? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null,
) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShapes.field)
            .background(colors.surface)
            .border(1.dp, colors.border, AppShapes.field)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(itemName, style = AppType.label, color = colors.textPrimary)
            Text(carrierName, style = AppType.caption, color = colors.textSecondary)
            if (stalledDays != null) {
                Spacer(Modifier.size(4.dp))
                StalledBadge(stalledDays)
            }
        }
        StatusPill(status)
        if (onDelete != null) {
            Icon(
                Icons.Outlined.Delete,
                contentDescription = strings.deleteParcel,
                tint = colors.textMuted,
                modifier = Modifier.padding(start = 8.dp).size(18.dp).clickable { onDelete() },
            )
        }
    }
}
