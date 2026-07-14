package com.parcelkr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.TrackingResult
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.DetailModel
import com.parcelkr.app.ui.components.PrimaryButton
import com.parcelkr.app.ui.components.ScreenHeader
import com.parcelkr.app.ui.components.SectionHeader
import com.parcelkr.app.ui.components.StatusPill
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun ContactScreen(
    trackingNumber: String,
    carrierName: String,
    model: DetailModel,
    onBack: () -> Unit,
    onCall: () -> Unit,
) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    var result by remember { mutableStateOf<TrackingResult?>(null) }
    var retryToken by remember { mutableStateOf(0) }
    val loadFailed by model.loadFailed.collectAsState()

    LaunchedEffect(trackingNumber, retryToken) {
        val carrier = Carrier.entries.firstOrNull { it.displayName == carrierName } ?: Carrier.CJ
        result = model.load(trackingNumber, carrier)
    }

    Column(Modifier.fillMaxSize().background(colors.bg)) {
        ScreenHeader(strings.contactDriver, onBack)
        when {
            result == null && !loadFailed -> {
                Box(Modifier.fillMaxWidth().padding(vertical = 48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.brand)
                }
            }
            result == null && loadFailed -> {
                Column(
                    Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(Icons.Outlined.ErrorOutline, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(30.dp))
                    Spacer(Modifier.size(10.dp))
                    Text(strings.trackingDetailFailedTitle, style = AppType.title, color = colors.textPrimary, textAlign = TextAlign.Center)
                    Spacer(Modifier.size(6.dp))
                    Text(strings.trackingDetailFailedMessage, style = AppType.caption, color = colors.textSecondary, textAlign = TextAlign.Center)
                    Spacer(Modifier.size(16.dp))
                    PrimaryButton(strings.retry, onClick = { retryToken++ }, leadingIcon = Icons.Outlined.Refresh)
                }
            }
            else -> {
                val r = result!!
                Column(
                    Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                        .clip(AppShapes.card).background(colors.surface)
                        .border(1.dp, colors.border, AppShapes.card).padding(14.dp),
                ) {
                    StatusPill(r.status)
                    Text("Driver · ${r.driverName ?: "—"}", style = AppType.body, color = colors.textPrimary, modifier = Modifier.padding(top = 6.dp))
                    Text("${r.itemName} · ${r.carrier.displayName}", style = AppType.caption, color = colors.textSecondary)
                }
                Spacer(Modifier.size(12.dp))
                if (r.driverPhone != null) {
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
                } else {
                    Text(
                        strings.noDriverInfo,
                        style = AppType.caption,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}
