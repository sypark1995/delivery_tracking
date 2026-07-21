package com.parcelkr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.parcelkr.app.domain.model.ForwardingParcel
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.ForwardingModel
import com.parcelkr.app.state.distinctForwardingTags
import com.parcelkr.app.state.filterForwardingByTag
import com.parcelkr.app.state.searchForwardingParcels
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
    val query by model.query.collectAsState()
    val tagFilter by model.tagFilter.collectAsState()
    var pendingDeleteId by remember { mutableStateOf<Long?>(null) }
    var pendingTagEditId by remember { mutableStateOf<Long?>(null) }

    Column(Modifier.fillMaxSize().background(colors.bg)) {
        ScreenHeader(
            strings.forwardingTracking, onBack, titleStyle = AppType.title,
            trailing = {
                Icon(
                    Icons.Outlined.Download,
                    contentDescription = strings.exportHistory,
                    tint = colors.textSecondary,
                    modifier = Modifier.clickable { model.exportCsv() }.padding(4.dp),
                )
            },
        )

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
                Row(
                    Modifier.fillMaxWidth()
                        .clip(AppShapes.field).background(colors.surface)
                        .border(1.dp, colors.border, AppShapes.field).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Search, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(8.dp))
                    BasicTextField(
                        value = query,
                        onValueChange = model::setQuery,
                        singleLine = true,
                        textStyle = TextStyle(color = colors.textPrimary, fontSize = AppType.body.fontSize),
                        cursorBrush = SolidColor(colors.brand),
                        modifier = Modifier.weight(1f),
                        decorationBox = { inner ->
                            if (query.isEmpty()) Text(strings.searchHint, style = AppType.body, color = colors.textMuted)
                            inner()
                        },
                    )
                }

                val tags = distinctForwardingTags(parcels)
                if (tags.isNotEmpty()) {
                    Row(
                        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val chips = listOf<String?>(null) + tags
                        chips.forEach { t ->
                            val on = t == tagFilter
                            Text(
                                t ?: strings.segAll,
                                style = AppType.caption,
                                color = if (on) Color.White else colors.textSecondary,
                                modifier = Modifier
                                    .clip(AppShapes.pill)
                                    .background(if (on) colors.brand else colors.segmentTrack)
                                    .clickable { model.setTagFilter(t) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                            )
                        }
                    }
                }

                val visible = searchForwardingParcels(filterForwardingByTag(parcels, tagFilter), query)
                if (query.isNotBlank() && visible.isEmpty()) {
                    Text(strings.noSearchResults, style = AppType.caption, color = colors.textSecondary, modifier = Modifier.padding(vertical = 8.dp))
                }
                visible.forEach { p ->
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
                            if (p.tag != null) {
                                Text(p.tag, style = AppType.caption, color = colors.brand, modifier = Modifier.padding(top = 2.dp))
                            }
                        }
                        Icon(
                            Icons.AutoMirrored.Outlined.Label,
                            contentDescription = strings.tagLabel,
                            tint = colors.textMuted,
                            modifier = Modifier.padding(start = 8.dp).size(18.dp).clickable { pendingTagEditId = p.id },
                        )
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

    val tagEditId = pendingTagEditId
    if (tagEditId != null) {
        var draft by remember(tagEditId) { mutableStateOf(parcels.firstOrNull { it.id == tagEditId }?.tag ?: "") }
        AlertDialog(
            onDismissRequest = { pendingTagEditId = null },
            title = { Text(strings.tagLabel) },
            text = {
                BasicTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    singleLine = true,
                    textStyle = TextStyle(color = colors.textPrimary, fontSize = AppType.body.fontSize),
                    cursorBrush = SolidColor(colors.brand),
                    decorationBox = { inner ->
                        if (draft.isEmpty()) Text(strings.addTagHint, style = AppType.body, color = colors.textMuted)
                        inner()
                    },
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    model.setTag(tagEditId, draft.trim().ifBlank { null })
                    pendingTagEditId = null
                }) { Text(strings.saveTag) }
            },
            dismissButton = {
                TextButton(onClick = { pendingTagEditId = null }) { Text(strings.cancel) }
            },
        )
    }
}
