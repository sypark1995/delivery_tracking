package com.parcelkr.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

/** Top bar with a localized back affordance and a title. Shared across detail-level screens. */
@Composable
fun ScreenHeader(title: String, onBack: () -> Unit, titleStyle: TextStyle = AppType.body) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = strings.back,
            tint = colors.textPrimary,
            modifier = Modifier.clickable { onBack() },
        )
        Spacer(Modifier.size(10.dp))
        Text(title, style = titleStyle, color = colors.textPrimary)
    }
}
