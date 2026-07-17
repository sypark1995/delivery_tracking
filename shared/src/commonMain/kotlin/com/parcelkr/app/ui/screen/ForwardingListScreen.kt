package com.parcelkr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.parcelkr.app.domain.model.ForwardingParcel
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.ForwardingModel
import com.parcelkr.app.ui.components.PrimaryButton
import com.parcelkr.app.ui.components.ScreenHeader
import com.parcelkr.app.ui.components.StatusPill
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun ForwardingListScreen(
    model: ForwardingModel,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onOpenParcel: (ForwardingParcel) -> Unit,
) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    val parcels by model.parcels.collectAsState()
    var pendingDeleteId by remember { mutableStateOf<Long?>(null) }

    Column(Modifier.fillMaxSize().background(colors.bg)) {
        ScreenHeader(strings.forwardingTracking, onBack, titleStyle = AppType.title)

        if (parcels.isEmpty()) {
            Column(
                Modifier.fillMaxWidth().padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier.padding(top = 8.dp).size(64.dp).clip(CircleShape).background(colors.brandTint),
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = colors.brand, modifier = Modifier.size(30.dp)) }
                Spacer(Modifier.height(10.dp))
                Text(strings.forwardingEmptyTitle, style = AppType.title, color = colors.textPrimary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(6.dp))
                Text(strings.forwardingEmptySubtitle, style = AppType.caption, color = colors.textSecondary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                PrimaryButton(strings.addForwardingParcel, onClick = onAdd, leadingIcon = Icons.Filled.Add)
            }
        } else {
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                parcels.forEach { p ->
                    Row(
                        Modifier.fillMaxWidth()
                            .clip(AppShapes.field).background(colors.surface)
                            .border(1.dp, colors.border, AppShapes.field)
                            .clickable { onOpenParcel(p) }.padding(13.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            StatusPill(p.overseasStatus)
                            Text(p.itemName, style = AppType.body, color = colors.textPrimary, modifier = Modifier.padding(top = 6.dp))
                            Text(
                                "${p.overseasCarrierName ?: p.overseasTrackingNumber} · ${if (p.domesticTrackingNumber != null) strings.domesticSection else strings.domesticNotYetAvailable}",
                                style = AppType.caption,
                                color = colors.textSecondary,
                            )
                        }
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = strings.deleteParcel,
                            tint = colors.textMuted,
                            modifier = Modifier.padding(start = 8.dp).size(18.dp).clickable { pendingDeleteId = p.id },
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                PrimaryButton(strings.addForwardingParcel, onClick = onAdd, leadingIcon = Icons.Filled.Add)
            }
        }
    }

    val deleteId = pendingDeleteId
    if (deleteId != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title = { Text(strings.deleteParcelConfirmTitle) },
            text = { Text(strings.deleteParcelConfirmMessage) },
            confirmButton = {
                TextButton(onClick = {
                    model.delete(deleteId)
                    pendingDeleteId = null
                }) { Text(strings.delete) }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteId = null }) { Text(strings.cancel) }
            },
        )
    }
}
