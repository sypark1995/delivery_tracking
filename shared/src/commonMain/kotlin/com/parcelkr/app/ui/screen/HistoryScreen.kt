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
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.HistoryModel
import com.parcelkr.app.ui.components.ScreenHeader
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun HistoryScreen(model: HistoryModel, onBack: () -> Unit) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    val counts by model.monthlyCounts.collectAsState()

    Column(Modifier.fillMaxSize().background(colors.bg)) {
        ScreenHeader(
            strings.deliveryHistory, onBack, titleStyle = AppType.title,
            trailing = {
                Icon(
                    Icons.Outlined.Download,
                    contentDescription = strings.exportHistory,
                    tint = colors.textSecondary,
                    modifier = Modifier.clickable { model.exportCsv() }.padding(4.dp),
                )
            },
        )
        if (counts.isEmpty()) {
            EmptyHistoryState()
        } else {
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                counts.forEach { mc ->
                    Row(
                        Modifier.fillMaxWidth()
                            .clip(AppShapes.field).background(colors.surface)
                            .border(1.dp, colors.border, AppShapes.field)
                            .padding(horizontal = 13.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(mc.month, style = AppType.body, color = colors.textPrimary, modifier = Modifier.weight(1f))
                        Text("${mc.count} ${strings.parcelsUnit}", style = AppType.caption, color = colors.textSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyHistoryState() {
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
        Text(strings.historyEmptyMessage, style = AppType.caption, color = colors.textSecondary, textAlign = TextAlign.Center)
    }
}
