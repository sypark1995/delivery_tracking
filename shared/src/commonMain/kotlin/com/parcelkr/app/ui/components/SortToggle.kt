package com.parcelkr.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.SortOrder
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun SortToggle(selected: SortOrder, onSelect: (SortOrder) -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    val items = listOf(
        SortOrder.RECENT to strings.sortByRecent,
        SortOrder.NAME to strings.sortByName,
    )
    Row(modifier = modifier) {
        items.forEach { (order, label) ->
            val on = order == selected
            Text(
                label,
                style = AppType.caption,
                color = if (on) colors.brand else colors.textSecondary,
                modifier = Modifier
                    .clip(AppShapes.pill)
                    .background(if (on) colors.brandTint else Color.Transparent)
                    .clickable { onSelect(order) }
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
    }
}
