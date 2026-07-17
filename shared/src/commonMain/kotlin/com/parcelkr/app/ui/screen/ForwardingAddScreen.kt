package com.parcelkr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.ForwardingModel
import com.parcelkr.app.ui.components.PrimaryButton
import com.parcelkr.app.ui.components.ScreenHeader
import com.parcelkr.app.ui.components.SectionHeader
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors
import kotlinx.coroutines.launch

@Composable
fun ForwardingAddScreen(model: ForwardingModel, onBack: () -> Unit, onAdded: () -> Unit) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    val addFailed by model.addFailed.collectAsState()
    val scope = rememberCoroutineScope()
    var itemName by remember { mutableStateOf("") }
    var trackingNumber by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(colors.bg)) {
        ScreenHeader(strings.addForwardingParcel, onBack)

        SectionHeader(strings.forwardingItemNameHint)
        TextInputField(
            value = itemName,
            onValueChange = { itemName = it },
            hint = strings.forwardingItemNameHint,
        )

        SectionHeader(strings.overseasTrackingNumberHint)
        TextInputField(
            value = trackingNumber,
            onValueChange = { trackingNumber = it },
            hint = strings.overseasTrackingNumberHint,
        )

        if (addFailed) {
            Text(
                strings.forwardingAddFailed,
                style = AppType.caption,
                color = colors.textSecondary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            )
        }

        PrimaryButton(
            strings.addForwardingParcel,
            onClick = {
                scope.launch {
                    if (model.addForwardingParcel(itemName.trim(), trackingNumber.trim()) != null) onAdded()
                }
            },
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun TextInputField(value: String, onValueChange: (String) -> Unit, hint: String) {
    val colors = LocalColors.current
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            .clip(AppShapes.field).background(colors.surface)
            .border(1.dp, colors.border, AppShapes.field).padding(12.dp),
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(color = colors.textPrimary, fontSize = AppType.body.fontSize),
            cursorBrush = SolidColor(colors.brand),
            decorationBox = { inner ->
                if (value.isEmpty()) Text(hint, style = AppType.body, color = colors.textMuted)
                inner()
            },
        )
    }
}
