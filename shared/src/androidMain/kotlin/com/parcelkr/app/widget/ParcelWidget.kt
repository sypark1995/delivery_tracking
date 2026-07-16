package com.parcelkr.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.unit.ColorProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.deviceLang
import com.parcelkr.app.i18n.Lang
import com.parcelkr.app.i18n.stringsFor
import com.parcelkr.app.state.heroOf
import com.parcelkr.app.ui.components.statusLabel
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private class WidgetPalette(
    val bg: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val brand: Color,
)

// Mirrors shared/src/commonMain/kotlin/com/parcelkr/app/ui/theme/Color.kt's LightColors/DarkColors.
private val WidgetLightPalette = WidgetPalette(
    bg = Color(0xFFF4F2ED),
    textPrimary = Color(0xFF17130E),
    textSecondary = Color(0xFF8A8375),
    brand = Color(0xFF0E8F6E),
)
private val WidgetDarkPalette = WidgetPalette(
    bg = Color(0xFF16130F),
    textPrimary = Color(0xFFF0ECE4),
    textSecondary = Color(0xFFA8A192),
    brand = Color(0xFF2FB98F),
)

private const val MAIN_ACTIVITY_CLASS_NAME = "com.parcelkr.app.MainActivity"
const val EXTRA_TRACKING_NUMBER = "com.parcelkr.app.extra.TRACKING_NUMBER"
const val EXTRA_CARRIER_NAME = "com.parcelkr.app.extra.CARRIER_NAME"

class ParcelWidget : GlanceAppWidget(), KoinComponent {
    private val repo: ParcelRepository by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val parcels = repo.observeParcels().first()
        val hero = heroOf(parcels)
        val lang = repo.savedLang()?.let { Lang.fromCode(it) } ?: deviceLang()
        val strings = stringsFor(lang)
        val palette = if (repo.isDarkMode()) WidgetDarkPalette else WidgetLightPalette
        // MainActivity lives in the androidApp module, which shared does not (and should not)
        // depend on. Android manifest/component resolution is package-name based, so we can
        // target it via an explicit component name instead of a compile-time class reference.
        val openAppIntent = Intent().setClassName(context.packageName, MAIN_ACTIVITY_CLASS_NAME).apply {
            if (hero != null) {
                putExtra(EXTRA_TRACKING_NUMBER, hero.trackingNumber)
                putExtra(EXTRA_CARRIER_NAME, hero.carrier.displayName)
            }
        }

        provideContent {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ColorProvider(palette.bg))
                    .padding(12.dp)
                    .clickable(actionStartActivity(openAppIntent)),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.Start,
            ) {
                if (hero != null) {
                    Text(
                        text = hero.itemName,
                        style = TextStyle(
                            color = ColorProvider(palette.textPrimary),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        ),
                        maxLines = 1,
                    )
                    Text(
                        text = "${hero.carrier.displayName} · ${statusLabel(hero.status, strings)}",
                        style = TextStyle(
                            color = ColorProvider(palette.textSecondary),
                            fontSize = 12.sp,
                        ),
                        maxLines = 1,
                    )
                    Text(
                        text = "${(hero.progress * 100).toInt()}%",
                        style = TextStyle(
                            color = ColorProvider(palette.brand),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                        ),
                    )
                } else {
                    Text(
                        text = strings.widgetNoActiveParcel,
                        style = TextStyle(
                            color = ColorProvider(palette.textPrimary),
                            fontSize = 13.sp,
                        ),
                    )
                }
            }
        }
    }
}
