package com.parcelkr.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.parcelkr.app.i18n.Lang
import com.parcelkr.app.i18n.LocalLang
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.i18n.stringsFor
import com.parcelkr.app.ui.screen.OnboardingScreen
import com.parcelkr.app.ui.theme.AppTheme

@Preview
@Composable
fun OnboardingPreview() {
    AppTheme(dark = false) {
        CompositionLocalProvider(LocalLang provides Lang.EN, LocalStrings provides stringsFor(Lang.EN)) {
            OnboardingScreen(selected = Lang.EN, onSelect = {}, onContinue = {})
        }
    }
}
