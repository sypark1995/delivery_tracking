package com.parcelkr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.parcelkr.app.domain.model.OverseasTrackingResult
import com.parcelkr.app.domain.model.TrackingResult
import com.parcelkr.app.i18n.LocalStrings
import com.parcelkr.app.state.ForwardingDetailModel
import com.parcelkr.app.state.ForwardingModel
import com.parcelkr.app.ui.components.PrimaryButton
import com.parcelkr.app.ui.components.ScreenHeader
import com.parcelkr.app.ui.components.SectionHeader
import com.parcelkr.app.ui.components.StatusPill
import com.parcelkr.app.ui.components.TimelineStep
import com.parcelkr.app.ui.components.statusColorsFor
import com.parcelkr.app.ui.theme.AppShapes
import com.parcelkr.app.ui.theme.AppType
import com.parcelkr.app.ui.theme.LocalColors
import kotlinx.coroutines.launch

@Composable
fun ForwardingDetailScreen(
    id: Long,
    model: ForwardingModel,
    detailModel: ForwardingDetailModel,
    onBack: () -> Unit,
) {
    val colors = LocalColors.current
    val strings = LocalStrings.current
    val parcels by model.parcels.collectAsState()
    val parcel = parcels.firstOrNull { it.id == id }
    val attachFailed by model.attachFailed.collectAsState()
    var overseasResult by remember { mutableStateOf<OverseasTrackingResult?>(null) }
    var domesticResult by remember { mutableStateOf<TrackingResult?>(null) }
    var domesticInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(parcel?.overseasTrackingNumber) {
        val number = parcel?.overseasTrackingNumber ?: return@LaunchedEffect
        overseasResult = detailModel.loadOverseas(number)
    }
    LaunchedEffect(parcel?.domesticTrackingNumber) {
        val number = parcel?.domesticTrackingNumber ?: return@LaunchedEffect
        val carrier = parcel.domesticCarrier ?: return@LaunchedEffect
        domesticResult = detailModel.loadDomestic(number, carrier)
    }

    if (parcel == null) {
        Column(Modifier.fillMaxSize().background(colors.bg)) {
            ScreenHeader(strings.forwardingTracking, onBack)
        }
        return
    }

    Column(Modifier.fillMaxSize().background(colors.bg).verticalScroll(rememberScrollState())) {
        ScreenHeader(parcel.itemName, onBack)

        Column(
            Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                .clip(AppShapes.card).background(colors.surface)
                .border(1.dp, colors.border, AppShapes.card).padding(14.dp),
        ) {
            StatusPill(parcel.overseasStatus)
            Text(parcel.itemName, style = AppType.title, color = colors.textPrimary, modifier = Modifier.padding(top = 6.dp))
            Text(
                parcel.overseasCarrierName ?: parcel.overseasTrackingNumber,
                style = AppType.caption,
                color = colors.textSecondary,
            )
        }

        SectionHeader(strings.overseasSection)
        Column(
            Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                .clip(AppShapes.card).background(colors.surface)
                .border(1.dp, colors.border, AppShapes.card).padding(14.dp),
        ) {
            val events = overseasResult?.events.orEmpty()
            events.forEachIndexed { i, e ->
                TimelineStep(
                    labelTranslated = e.description,
                    labelKo = "",
                    timePlace = if (e.location != null) "${e.timeText} · ${e.location}" else e.timeText,
                    dotColor = statusColorsFor(e.status).dot,
                    filled = i == 0,
                    showOriginal = false,
                    isLast = i == events.lastIndex,
                )
            }
        }

        Spacer(Modifier.height(4.dp))
        SectionHeader(strings.domesticSection)
        val domesticTrackingNumber = parcel.domesticTrackingNumber
        if (domesticTrackingNumber == null) {
            Column(Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                Text(strings.domesticNotYetAvailable, style = AppType.caption, color = colors.textSecondary)
                Spacer(Modifier.height(10.dp))
                Column(
                    Modifier.fillMaxWidth()
                        .clip(AppShapes.field).background(colors.surface)
                        .border(1.dp, colors.border, AppShapes.field).padding(12.dp),
                ) {
                    BasicTextField(
                        value = domesticInput,
                        onValueChange = { domesticInput = it },
                        singleLine = true,
                        textStyle = TextStyle(color = colors.textPrimary, fontSize = AppType.body.fontSize),
                        cursorBrush = SolidColor(colors.brand),
                        decorationBox = { inner ->
                            if (domesticInput.isEmpty()) Text(strings.domesticTrackingNumberHint, style = AppType.body, color = colors.textMuted)
                            inner()
                        },
                    )
                }
                if (attachFailed) {
                    Spacer(Modifier.height(6.dp))
                    Text(strings.trackingLookupFailed, style = AppType.caption, color = colors.textSecondary)
                }
                Spacer(Modifier.height(10.dp))
                PrimaryButton(
                    strings.saveDomesticTracking,
                    onClick = { scope.launch { model.attachDomestic(parcel.id, domesticInput.trim()) } },
                )
            }
        } else {
            Column(
                Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                    .clip(AppShapes.card).background(colors.surface)
                    .border(1.dp, colors.border, AppShapes.card).padding(14.dp),
            ) {
                val events = domesticResult?.events.orEmpty()
                events.forEachIndexed { i, e ->
                    TimelineStep(
                        labelTranslated = e.labelKo,
                        labelKo = e.labelKo,
                        timePlace = "${e.timeText} · ${e.place}",
                        dotColor = statusColorsFor(e.status).dot,
                        filled = i == 0,
                        showOriginal = false,
                        isLast = i == events.lastIndex,
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}
