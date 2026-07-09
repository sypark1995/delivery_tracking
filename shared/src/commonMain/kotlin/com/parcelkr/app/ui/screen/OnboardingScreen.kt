package com.parcelkr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.parcelkr.app.i18n.Lang
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.ui.components.PrimaryButton
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors

@Composable
fun OnboardingScreen(selected: Lang, onSelect: (Lang) -> Unit, onContinue: () -> Unit) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    Column(Modifier.fillMaxSize().background(colors.bg).padding(16.dp)) {
        Text(strings.appName, style = AppType.title, color = colors.textPrimary, modifier = Modifier.padding(top = 24.dp))
        Text(strings.onboardingTagline, style = AppType.body, color = colors.textSecondary, modifier = Modifier.padding(top = 6.dp, bottom = 20.dp))
        Text(strings.chooseLanguage, style = AppType.label, color = colors.textSecondary, modifier = Modifier.padding(bottom = 8.dp))
        Lang.entries.forEach { lang ->
            val on = lang == selected
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clip(AppShapes.field)
                    .background(if (on) colors.brandTint else colors.surface)
                    .border(1.dp, if (on) colors.brand else colors.border, AppShapes.field)
                    .clickable { onSelect(lang) }
                    .padding(horizontal = 13.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(lang.nativeName, style = AppType.body, color = colors.textPrimary, modifier = Modifier.weight(1f))
                if (on) Icon(Icons.Filled.Check, contentDescription = null, tint = colors.brand)
                else Text(lang.englishName, style = AppType.caption, color = colors.textSecondary)
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            PrimaryButton(strings.cont, onClick = onContinue, leadingIcon = Icons.AutoMirrored.Filled.ArrowForward)
        }
    }
}
