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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.ui.components.PrimaryButton
import com.parcelkr.app.ui.components.SectionHeader
import com.parcelkr.app.ui.components.StatusPill
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun ContactScreen(driverName: String, itemName: String, carrierName: String, onBack: () -> Unit, onCall: () -> Unit) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    Column(Modifier.fillMaxSize().background(colors.bg)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = colors.textPrimary,
                modifier = Modifier.clickable { onBack() })
            Spacer(Modifier.size(10.dp))
            Text(strings.contactDriver, style = AppType.body, color = colors.textPrimary)
        }
        Column(
            Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                .clip(AppShapes.card).background(colors.surface)
                .border(1.dp, colors.border, AppShapes.card).padding(14.dp),
        ) {
            StatusPill(DeliveryStatus.OUT_FOR_DELIVERY)
            Text("Driver · $driverName", style = AppType.body, color = colors.textPrimary, modifier = Modifier.padding(top = 6.dp))
            Text("$itemName · $carrierName", style = AppType.caption, color = colors.textSecondary)
        }
        Spacer(Modifier.size(12.dp))
        PrimaryButton(strings.callDriver, onClick = onCall, leadingIcon = Icons.Outlined.Phone, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.size(12.dp))
        SectionHeader(strings.sendMessageAutoTranslated)
        Column(
            Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                .clip(AppShapes.field).background(colors.brandTint)
                .border(1.dp, colors.brand.copy(alpha = 0.4f), AppShapes.field).padding(11.dp),
        ) {
            Text(strings.you, style = AppType.caption, color = colors.textSecondary)
            Text("Please leave it at the door.", style = AppType.body, color = colors.textPrimary)
            Spacer(Modifier.size(6.dp))
            Text(strings.sentInKorean, style = AppType.caption, color = colors.textSecondary)
            Text("문 앞에 놓아주세요.", style = AppType.body, color = colors.onTint)
        }
    }
}
