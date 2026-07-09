package com.parcelkr.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun TimelineStep(
    labelTranslated: String,
    labelKo: String,
    timePlace: String,
    dotColor: Color,
    filled: Boolean,
    showOriginal: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColors.current
    Row(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(14.dp)) {
            Box(
                Modifier
                    .padding(top = 2.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (filled) dotColor else colors.textMuted),
            )
            if (!isLast) {
                Box(Modifier.width(2.dp).height(36.dp).padding(top = 2.dp).background(colors.border))
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.padding(bottom = 13.dp)) {
            Text(labelTranslated, style = AppType.label, color = if (filled) dotColor else colors.textSecondary)
            if (showOriginal) Text(labelKo, style = AppType.caption, color = colors.textMuted)
            Text(timePlace, style = AppType.caption, color = colors.textSecondary)
        }
    }
}
