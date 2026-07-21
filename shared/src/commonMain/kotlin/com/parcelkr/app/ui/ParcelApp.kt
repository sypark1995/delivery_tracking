package com.parcelkr.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.parcelkr.app.data.ForwardingParcelRepository
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.deviceLang
import com.parcelkr.app.domain.CarrierDetector
import com.parcelkr.app.domain.CsvExporter
import com.parcelkr.app.domain.DialerLauncher
import com.parcelkr.app.domain.OverseasTrackingApi
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.UrlLauncher
import com.parcelkr.app.i18n.Lang
import com.parcelkr.app.i18n.LocalLang
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.i18n.stringsFor
import com.parcelkr.app.state.AddModel
import com.parcelkr.app.state.DetailModel
import com.parcelkr.app.state.ForwardingDetailModel
import com.parcelkr.app.state.ForwardingModel
import com.parcelkr.app.state.HistoryModel
import com.parcelkr.app.state.HomeModel
import com.parcelkr.app.ui.screen.AddScreen
import com.parcelkr.app.ui.screen.ContactScreen
import com.parcelkr.app.ui.screen.DetailScreen
import com.parcelkr.app.ui.screen.ForwardingAddScreen
import com.parcelkr.app.ui.screen.ForwardingDetailScreen
import com.parcelkr.app.ui.screen.ForwardingListScreen
import com.parcelkr.app.ui.screen.HistoryScreen
import com.parcelkr.app.ui.screen.HomeScreen
import com.parcelkr.app.ui.screen.OnboardingScreen
import com.parcelkr.app.ui.screen.SettingsScreen
import com.parcelkr.app.ui.screen.UpdatesScreen
import com.parcelkr.app.ui.theme.AppTheme
import com.parcelkr.app.ui.theme.LocalColors
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun ParcelApp(
    initialSharedText: String? = null,
    openAddDirectly: Boolean = false,
    initialDetailTrackingNumber: String? = null,
    initialDetailCarrierName: String? = null,
) {
    val repo = koinInject<ParcelRepository>()
    var lang by remember { mutableStateOf(repo.savedLang()?.let { Lang.fromCode(it) } ?: deviceLang()) }
    var dark by remember { mutableStateOf(repo.isDarkMode()) }
    var notificationsEnabled by remember { mutableStateOf(repo.notificationsEnabled()) }
    var dndEnabled by remember { mutableStateOf(repo.dndEnabled()) }
    var dndStartMinute by remember { mutableStateOf(repo.dndStartMinute()) }
    var dndEndMinute by remember { mutableStateOf(repo.dndEndMinute()) }
    var current by remember { mutableStateOf<Screen>(if (repo.isOnboardingDone()) Screen.Home else Screen.Onboarding) }

    val detector = koinInject<CarrierDetector>()
    val api = koinInject<TrackingApi>()
    val dialer = koinInject<DialerLauncher>()
    val urlLauncher = koinInject<UrlLauncher>()
    val csvExporter = koinInject<CsvExporter>()
    val forwardingRepo = koinInject<ForwardingParcelRepository>()
    val overseasApi = koinInject<OverseasTrackingApi>()
    val scope = rememberCoroutineScope()
    val homeModel = remember { HomeModel(repo, api, scope) }
    val cachedParcels by homeModel.parcels.collectAsState()
    val addModel = remember { AddModel(repo, detector, api) }
    val forwardingModel = remember { ForwardingModel(forwardingRepo, overseasApi, api, detector, csvExporter, scope) }
    var customsCode by remember { mutableStateOf(repo.customsCode()) }

    LaunchedEffect(Unit) {
        if (initialSharedText != null && repo.isOnboardingDone()) {
            current = Screen.Add
            addModel.onPasteEmail(initialSharedText)
        } else if (openAddDirectly && repo.isOnboardingDone()) {
            current = Screen.Add
        } else if (initialDetailTrackingNumber != null && initialDetailCarrierName != null && repo.isOnboardingDone()) {
            current = Screen.Detail(initialDetailTrackingNumber, initialDetailCarrierName)
        }
    }

    PlatformBackHandler(enabled = backTargetFor(current) != null) {
        backTargetFor(current)?.let { current = it }
    }

    AppTheme(dark = dark) {
        CompositionLocalProvider(
            LocalLang provides lang,
            LocalStrings provides stringsFor(lang),
        ) {
            Box(Modifier.fillMaxSize().background(LocalColors.current.bg).safeDrawingPadding()) {
                when (val screen = current) {
                    Screen.Onboarding -> OnboardingScreen(
                        selected = lang,
                        onSelect = {
                            lang = it
                            scope.launch { repo.setLang(it.code) }
                        },
                        onContinue = {
                            current = Screen.Home
                            scope.launch { repo.setOnboardingDone() }
                        },
                    )
                    Screen.Home -> HomeScreen(
                        model = homeModel,
                        onAdd = { current = Screen.Add },
                        onOpenParcel = { current = Screen.Detail(it.trackingNumber, it.carrier.displayName) },
                        onCallDriver = { current = Screen.Contact(it.trackingNumber, it.carrier.displayName) },
                        onOpenUpdates = { current = Screen.Updates },
                        onOpenSettings = { current = Screen.Settings },
                    )
                    Screen.Add -> AddScreen(
                        model = addModel,
                        onBack = { current = Screen.Home },
                        onAdded = { current = Screen.Home },
                    )
                    is Screen.Detail -> DetailScreen(
                        trackingNumber = screen.trackingNumber,
                        carrierName = screen.carrierName,
                        cachedParcel = cachedParcels.firstOrNull { it.trackingNumber == screen.trackingNumber },
                        model = remember { DetailModel(api) },
                        urlLauncher = urlLauncher,
                        onBack = { current = Screen.Home },
                        onContactDriver = { current = Screen.Contact(screen.trackingNumber, screen.carrierName) },
                    )
                    is Screen.Contact -> ContactScreen(
                        trackingNumber = screen.trackingNumber,
                        carrierName = screen.carrierName,
                        model = remember { DetailModel(api) },
                        dialer = dialer,
                        onBack = { current = Screen.Home },
                    )
                    Screen.Settings -> SettingsScreen(
                        currentLang = lang,
                        dark = dark,
                        notificationsEnabled = notificationsEnabled,
                        dndEnabled = dndEnabled,
                        dndStartMinute = dndStartMinute,
                        dndEndMinute = dndEndMinute,
                        customsCode = customsCode,
                        onBack = { current = Screen.Home },
                        onPickLanguage = {
                            lang = it
                            scope.launch { repo.setLang(it.code) }
                        },
                        onToggleTheme = {
                            val newValue = !dark
                            dark = newValue
                            scope.launch { repo.setDarkMode(newValue) }
                        },
                        onToggleNotifications = {
                            val newValue = !notificationsEnabled
                            notificationsEnabled = newValue
                            scope.launch { repo.setNotificationsEnabled(newValue) }
                        },
                        onToggleDnd = {
                            val newValue = !dndEnabled
                            dndEnabled = newValue
                            scope.launch { repo.setDndEnabled(newValue) }
                        },
                        onSetDndWindow = { start, end ->
                            dndStartMinute = start
                            dndEndMinute = end
                            scope.launch {
                                repo.setDndStartMinute(start)
                                repo.setDndEndMinute(end)
                            }
                        },
                        onSetCustomsCode = { code ->
                            customsCode = code
                            scope.launch { repo.setCustomsCode(code) }
                        },
                        onOpenHistory = { current = Screen.History },
                        onOpenForwarding = { current = Screen.Forwarding },
                    )
                    Screen.Updates -> UpdatesScreen(onBack = { current = Screen.Home })
                    Screen.History -> HistoryScreen(
                        model = remember { HistoryModel(repo, csvExporter, scope) },
                        onBack = { current = Screen.Settings },
                    )
                    Screen.Forwarding -> ForwardingListScreen(
                        model = forwardingModel,
                        onBack = { current = Screen.Settings },
                        onAdd = { current = Screen.ForwardingAdd },
                        onOpenParcel = { current = Screen.ForwardingDetail(it.id) },
                    )
                    Screen.ForwardingAdd -> ForwardingAddScreen(
                        model = forwardingModel,
                        onBack = { current = Screen.Forwarding },
                        onAdded = { current = Screen.Forwarding },
                    )
                    is Screen.ForwardingDetail -> ForwardingDetailScreen(
                        id = screen.id,
                        model = forwardingModel,
                        detailModel = remember { ForwardingDetailModel(overseasApi, api) },
                        onBack = { current = Screen.Forwarding },
                    )
                }
            }
        }
    }
}
