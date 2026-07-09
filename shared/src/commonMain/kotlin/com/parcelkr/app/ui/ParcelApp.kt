package com.parcelkr.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.deviceLang
import com.parcelkr.app.domain.CarrierDetector
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.i18n.LocalLang
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.i18n.stringsFor
import com.parcelkr.app.state.AddModel
import com.parcelkr.app.state.DetailModel
import com.parcelkr.app.state.HomeModel
import com.parcelkr.app.ui.screen.AddScreen
import com.parcelkr.app.ui.screen.ContactScreen
import com.parcelkr.app.ui.screen.DetailScreen
import com.parcelkr.app.ui.screen.HomeScreen
import com.parcelkr.app.ui.screen.OnboardingScreen
import com.parcelkr.app.ui.screen.SettingsScreen
import com.parcelkr.app.ui.screen.UpdatesScreen
import com.parcelkr.app.ui.theme.AppTheme
import com.parcelkr.app.ui.theme.LocalColors
import org.koin.compose.koinInject

@Composable
fun ParcelApp() {
    var lang by remember { mutableStateOf(deviceLang()) }
    var dark by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf<Screen>(Screen.Onboarding) }

    val repo = koinInject<ParcelRepository>()
    val detector = koinInject<CarrierDetector>()
    val api = koinInject<TrackingApi>()
    val scope = rememberCoroutineScope()
    val homeModel = remember { HomeModel(repo, scope) }
    var customsCode by remember { mutableStateOf(repo.customsCode()) }

    AppTheme(dark = dark) {
        CompositionLocalProvider(
            LocalLang provides lang,
            LocalStrings provides stringsFor(lang),
        ) {
            Box(Modifier.fillMaxSize().background(LocalColors.current.bg)) {
                when (val screen = current) {
                    Screen.Onboarding -> OnboardingScreen(
                        selected = lang,
                        onSelect = { lang = it },
                        onContinue = { current = Screen.Home },
                    )
                    Screen.Home -> HomeScreen(
                        model = homeModel,
                        onAdd = { current = Screen.Add },
                        onOpenParcel = { current = Screen.Detail(it.trackingNumber, it.carrier.displayName) },
                        onOpenUpdates = { current = Screen.Updates },
                        onOpenSettings = { current = Screen.Settings },
                    )
                    Screen.Add -> AddScreen(
                        model = remember { AddModel(repo, detector, api, scope) },
                        onBack = { current = Screen.Home },
                        onAdded = { current = Screen.Home },
                    )
                    is Screen.Detail -> DetailScreen(
                        trackingNumber = screen.trackingNumber,
                        carrierName = screen.carrierName,
                        model = remember { DetailModel(api, scope) },
                        onBack = { current = Screen.Home },
                        onContactDriver = { current = Screen.Contact },
                    )
                    Screen.Contact -> ContactScreen(
                        driverName = "Kim Minsu",
                        itemName = "Nike Air Max",
                        carrierName = "CJ Logistics",
                        onBack = { current = Screen.Home },
                        onCall = {},
                    )
                    Screen.Settings -> SettingsScreen(
                        currentLang = lang,
                        dark = dark,
                        customsCode = customsCode,
                        onBack = { current = Screen.Home },
                        onPickLanguage = { lang = it },
                        onToggleTheme = { dark = !dark },
                    )
                    Screen.Updates -> UpdatesScreen(onBack = { current = Screen.Home })
                }
            }
        }
    }
}
