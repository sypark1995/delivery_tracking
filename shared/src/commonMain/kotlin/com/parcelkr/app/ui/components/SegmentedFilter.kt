package com.parcelkr.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.Segment
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun SegmentedFilter(selected: Segment, onSelect: (Segment) -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    val items = listOf(
        Segment.ACTIVE to strings.segActive,
        Segment.DELIVERED to strings.segDelivered,
        Segment.ALL to strings.segAll,
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShapes.segment)
            .background(colors.segmentTrack)
            .padding(3.dp),
    ) {
        items.forEach { (seg, label) ->
            val on = seg == selected
            Text(
                label,
                style = AppType.label,
                color = if (on) Color.White else colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(AppShapes.segment)
                    .background(if (on) colors.brand else Color.Transparent)
                    .clickable { onSelect(seg) }
                    .padding(vertical = 6.dp),
            )
        }
    }
}
