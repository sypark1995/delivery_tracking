package com.parcelkr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.AddModel
import com.parcelkr.app.ui.components.PrimaryButton
import com.parcelkr.app.ui.components.ScreenHeader
import com.parcelkr.app.ui.components.SectionHeader
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors
import kotlinx.coroutines.launch

@Composable
fun AddScreen(model: AddModel, onBack: () -> Unit, onAdded: () -> Unit) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    val input by model.input.collectAsState()
    val guess by model.guess.collectAsState()
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().background(colors.bg)) {
        ScreenHeader(strings.addTrackingBar, onBack)
        SectionHeader(strings.trackingNumber)
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                .clip(AppShapes.field).background(colors.surface)
                .border(1.dp, colors.border, AppShapes.field).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = input,
                onValueChange = model::onInput,
                singleLine = true,
                textStyle = TextStyle(color = colors.textPrimary, fontSize = AppType.body.fontSize),
                cursorBrush = SolidColor(colors.brand),
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (input.isEmpty()) Text(strings.trackingNumber, style = AppType.body, color = colors.textMuted)
                    inner()
                },
            )
        }
        guess?.let { g ->
            Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = colors.brand, modifier = Modifier.size(16.dp))
                Spacer(Modifier.size(7.dp))
                Text("${strings.detectedCarrier}: ${g.carrier.displayName}", style = AppType.caption, color = colors.textSecondary)
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            PrimaryButton(
                strings.addParcel,
                onClick = { scope.launch { if (model.confirmAdd() != null) onAdded() } },
                leadingIcon = Icons.Filled.Add,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
