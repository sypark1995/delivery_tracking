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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
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
    val pasteFailed by model.pasteFailed.collectAsState()
    val trackingFailed by model.trackingFailed.collectAsState()
    val scope = rememberCoroutineScope()
    var pasteFieldOpen by remember { mutableStateOf(false) }
    var pasteText by remember { mutableStateOf("") }

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
        SectionHeader(strings.addAnotherWay)
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MethodTile(
                label = strings.paste,
                icon = Icons.Outlined.ContentPaste,
                onClick = { pasteFieldOpen = !pasteFieldOpen },
                modifier = Modifier.weight(1f),
            )
            MethodTile(
                label = strings.scan,
                icon = Icons.Outlined.QrCodeScanner,
                onClick = {},
                modifier = Modifier.weight(1f),
            )
            MethodTile(
                label = strings.manual,
                icon = Icons.Outlined.Edit,
                onClick = {},
                modifier = Modifier.weight(1f),
            )
        }
        if (pasteFieldOpen) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
                Column(
                    Modifier.fillMaxWidth()
                        .clip(AppShapes.field).background(colors.surface)
                        .border(1.dp, colors.border, AppShapes.field).padding(12.dp),
                ) {
                    BasicTextField(
                        value = pasteText,
                        onValueChange = { pasteText = it },
                        textStyle = TextStyle(color = colors.textPrimary, fontSize = AppType.body.fontSize),
                        cursorBrush = SolidColor(colors.brand),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 96.dp),
                        decorationBox = { inner ->
                            if (pasteText.isEmpty()) Text(strings.pasteEmailHint, style = AppType.body, color = colors.textMuted)
                            inner()
                        },
                    )
                }
                if (pasteFailed) {
                    Text(
                        strings.pasteEmailFailed,
                        style = AppType.caption,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = 6.dp, start = 4.dp),
                    )
                }
                PrimaryButton(
                    strings.pasteEmailConfirm,
                    onClick = {
                        model.onPasteEmail(pasteText)
                        if (!model.pasteFailed.value) {
                            pasteFieldOpen = false
                            pasteText = ""
                        }
                    },
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            if (trackingFailed) {
                Text(
                    strings.trackingLookupFailed,
                    style = AppType.caption,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                )
            }
            PrimaryButton(
                strings.addParcel,
                onClick = { scope.launch { if (model.confirmAdd() != null) onAdded() } },
                leadingIcon = Icons.Filled.Add,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun MethodTile(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColors.current
    Column(
        modifier = modifier
            .clip(AppShapes.field)
            .background(colors.surface)
            .border(1.dp, colors.border, AppShapes.field)
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(icon, contentDescription = null, tint = colors.brand, modifier = Modifier.size(20.dp))
        Spacer(Modifier.size(6.dp))
        Text(label, style = AppType.label, color = colors.textPrimary)
    }
}
