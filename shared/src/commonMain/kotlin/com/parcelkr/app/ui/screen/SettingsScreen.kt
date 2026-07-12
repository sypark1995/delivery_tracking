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
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.Lang
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.ui.components.ScreenHeader
import com.parcelkr.app.ui.components.SectionHeader
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(
    currentLang: Lang,
    dark: Boolean,
    customsCode: String?,
    onBack: () -> Unit,
    onPickLanguage: (Lang) -> Unit,
    onToggleTheme: () -> Unit,
    onSetCustomsCode: (String) -> Unit,
) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    Column(Modifier.fillMaxSize().background(colors.bg)) {
        ScreenHeader(strings.settings, onBack, titleStyle = AppType.title)
        SectionHeader(strings.customs)
        CustomsCodeRow(customsCode = customsCode, onSave = onSetCustomsCode)
        SectionHeader(strings.preferences)
        val nextLang = Lang.entries[(currentLang.ordinal + 1) % Lang.entries.size]
        SettingRow(Icons.Outlined.Language, strings.language, onClick = { onPickLanguage(nextLang) }) {
            Text(currentLang.nativeName, style = AppType.caption, color = colors.textSecondary)
        }
        SettingRow(Icons.Outlined.Notifications, strings.notifications) {
            Switch(checked = true, onCheckedChange = null)
        }
        SettingRow(Icons.Outlined.DarkMode, strings.theme, onClick = onToggleTheme) {
            Switch(checked = dark, onCheckedChange = { onToggleTheme() })
        }
        SectionHeader(strings.about)
        SettingRow(Icons.Outlined.Badge, strings.version) {
            Text("1.0.0", style = AppType.caption, color = colors.textSecondary)
        }
    }
}

private fun mask(code: String): String = if (code.length <= 4) code else code.take(5) + "•••"

@Composable
private fun CustomsCodeRow(customsCode: String?, onSave: (String) -> Unit) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    val clipboard = LocalClipboardManager.current
    var editing by remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf(customsCode ?: "") }
    var copied by remember { mutableStateOf(false) }

    LaunchedEffect(copied) {
        if (copied) {
            delay(1500)
            copied = false
        }
    }

    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(AppShapes.field).background(colors.surface)
            .border(1.dp, colors.border, AppShapes.field)
            .let {
                if (!editing) {
                    it.clickable {
                        draft = customsCode ?: ""
                        editing = true
                    }
                } else it
            }
            .padding(horizontal = 13.dp, vertical = 12.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Badge, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(18.dp))
            Spacer(Modifier.size(11.dp))
            Text(strings.personalClearanceCode, style = AppType.body, color = colors.textPrimary, modifier = Modifier.weight(1f))
            if (!editing) {
                Text(
                    if (copied) strings.customsCodeCopied else (customsCode?.let { mask(it) } ?: "—"),
                    style = AppType.caption,
                    color = if (copied) colors.brand else colors.textSecondary,
                )
                if (!customsCode.isNullOrBlank()) {
                    Spacer(Modifier.size(8.dp))
                    Icon(
                        Icons.Outlined.ContentCopy,
                        contentDescription = strings.copyCustomsCode,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(16.dp).clickable {
                            clipboard.setText(AnnotatedString(customsCode))
                            copied = true
                        },
                    )
                }
            }
        }
        if (editing) {
            Spacer(Modifier.size(8.dp))
            Row(
                Modifier.fillMaxWidth()
                    .clip(AppShapes.field).background(colors.bg)
                    .border(1.dp, colors.border, AppShapes.field).padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    singleLine = true,
                    textStyle = TextStyle(color = colors.textPrimary, fontSize = AppType.body.fontSize),
                    cursorBrush = SolidColor(colors.brand),
                    modifier = Modifier.weight(1f),
                    decorationBox = { inner ->
                        if (draft.isEmpty()) Text(strings.enterCustomsCode, style = AppType.body, color = colors.textMuted)
                        inner()
                    },
                )
            }
            Spacer(Modifier.size(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    strings.saveCustomsCode,
                    style = AppType.label,
                    color = colors.brand,
                    modifier = Modifier.clickable {
                        onSave(draft.trim())
                        editing = false
                    }.padding(8.dp),
                )
            }
        }
    }
}

@Composable
private fun SettingRow(icon: ImageVector, label: String, onClick: (() -> Unit)? = null, trailing: @Composable () -> Unit) {
    val colors = LocalColors.current
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(AppShapes.field).background(colors.surface)
            .border(1.dp, colors.border, AppShapes.field)
            .let { if (onClick != null) it.clickable { onClick() } else it }
            .padding(horizontal = 13.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.size(11.dp))
        Text(label, style = AppType.body, color = colors.textPrimary, modifier = Modifier.weight(1f))
        trailing()
    }
}
