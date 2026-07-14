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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
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
import com.parcelkr.app.domain.model.Parcel
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.HomeModel
import com.parcelkr.app.state.Segment
import com.parcelkr.app.state.filterBySegment
import com.parcelkr.app.state.heroOf
import com.parcelkr.app.ui.components.ParcelCard
import com.parcelkr.app.ui.components.PrimaryButton
import com.parcelkr.app.ui.components.SectionHeader
import com.parcelkr.app.ui.components.SegmentedFilter
import com.parcelkr.app.ui.components.StatusPill
import com.parcelkr.app.ui.components.statusColorsFor
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun HomeScreen(
    model: HomeModel,
    onAdd: () -> Unit,
    onOpenParcel: (Parcel) -> Unit,
    onCallDriver: (Parcel) -> Unit,
    onOpenUpdates: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    val parcels by model.parcels.collectAsState()
    val segment by model.segment.collectAsState()
    val hero = heroOf(parcels)
    // The hero is the live in-progress shipment; it headlines Active/All but not the Delivered filter.
    val showHero = hero != null && segment != Segment.DELIVERED
    var pendingDeleteId by remember { mutableStateOf<Long?>(null) }

    Column(Modifier.fillMaxSize().background(colors.bg).verticalScroll(rememberScrollState())) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(strings.homeTitle, style = AppType.title, color = colors.textPrimary, modifier = Modifier.weight(1f))
            Icon(Icons.Outlined.Notifications, contentDescription = strings.updates, tint = colors.textSecondary,
                modifier = Modifier.clickable { onOpenUpdates() }.padding(4.dp))
            Spacer(Modifier.size(8.dp))
            Icon(Icons.Outlined.Settings, contentDescription = strings.settings, tint = colors.textSecondary,
                modifier = Modifier.clickable { onOpenSettings() }.padding(4.dp))
        }

        if (parcels.isEmpty()) {
            EmptyState(onAdd)
            return@Column
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                .clip(AppShapes.field).background(colors.surface)
                .border(1.dp, colors.border, AppShapes.field)
                .clickable { onAdd() }.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.Search, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(18.dp))
            Spacer(Modifier.size(8.dp))
            Text(strings.addTrackingBar, style = AppType.caption, color = colors.textMuted)
        }
        Spacer(Modifier.height(12.dp))

        SegmentedFilter(segment, model::setSegment, Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(12.dp))

        if (showHero && hero != null) {
            val sc = statusColorsFor(hero.status)
            Column(
                Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                    .clip(AppShapes.hero).background(colors.surface)
                    .border(1.dp, colors.border, AppShapes.hero)
                    .clickable { onOpenParcel(hero) }.padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                StatusPill(hero.status)
                Text(hero.etaText ?: strings.expectedToday, style = AppType.display, color = colors.textPrimary)
                Text("${hero.itemName} · ${hero.carrier.displayName}", style = AppType.caption, color = colors.textSecondary)
                Box(Modifier.fillMaxWidth().height(6.dp).clip(AppShapes.pill).background(colors.segmentTrack)) {
                    Box(Modifier.fillMaxWidth(hero.progress).height(6.dp).clip(AppShapes.pill).background(sc.dot))
                }
                PrimaryButton(strings.callDriver, onClick = { onCallDriver(hero) }, leadingIcon = Icons.Outlined.Phone)
            }
            Spacer(Modifier.height(4.dp))
        }

        SectionHeader(strings.recent)
        Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            filterBySegment(parcels, segment)
                .filter { !showHero || it.id != hero?.id }
                .forEach { p ->
                    ParcelCard(
                        p.itemName, p.carrier.displayName, p.status,
                        onClick = { onOpenParcel(p) },
                        onDelete = { pendingDeleteId = p.id },
                    )
                }
        }
        Spacer(Modifier.height(24.dp))
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

@Composable
private fun EmptyState(onAdd: () -> Unit) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    Column(
        Modifier.fillMaxWidth().padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.padding(top = 8.dp).size(64.dp).clip(CircleShape).background(colors.brandTint),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = colors.brand, modifier = Modifier.size(30.dp)) }
        Spacer(Modifier.height(10.dp))
        Text(strings.emptyTitle, style = AppType.title, color = colors.textPrimary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(6.dp))
        Text(strings.emptySubtitle, style = AppType.caption, color = colors.textSecondary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        PrimaryButton(strings.addParcel, onClick = onAdd, leadingIcon = Icons.Filled.Add)
    }
}
