package com.parcelkr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.Lang
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.ui.components.SectionHeader
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun SettingsScreen(
    currentLang: Lang,
    dark: Boolean,
    customsCode: String?,
    onBack: () -> Unit,
    onPickLanguage: (Lang) -> Unit,
    onToggleTheme: () -> Unit,
) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    Column(Modifier.fillMaxSize().background(colors.bg)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = colors.textPrimary,
                modifier = Modifier.clickable { onBack() })
            Spacer(Modifier.size(10.dp))
            Text(strings.settings, style = AppType.title, color = colors.textPrimary)
        }
        SectionHeader(strings.customs)
        SettingRow(Icons.Outlined.Badge, strings.personalClearanceCode) {
            Text(customsCode?.let { mask(it) } ?: "—", style = AppType.caption, color = colors.textSecondary)
        }
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
