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

private val WidgetBg = Color(0xFFF4F2ED)
private val WidgetTextPrimary = Color(0xFF17130E)
private val WidgetTextSecondary = Color(0xFF8A8375)
private val WidgetBrand = Color(0xFF0E8F6E)

private const val MAIN_ACTIVITY_CLASS_NAME = "com.parcelkr.app.MainActivity"

class ParcelWidget : GlanceAppWidget(), KoinComponent {
    private val repo: ParcelRepository by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val parcels = repo.observeParcels().first()
        val hero = heroOf(parcels)
        val lang = repo.savedLang()?.let { Lang.fromCode(it) } ?: deviceLang()
        val strings = stringsFor(lang)
        // MainActivity lives in the androidApp module, which shared does not (and should not)
        // depend on. Android manifest/component resolution is package-name based, so we can
        // target it via an explicit component name instead of a compile-time class reference.
        val openAppIntent = Intent().setClassName(context.packageName, MAIN_ACTIVITY_CLASS_NAME)

        provideContent {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ColorProvider(WidgetBg))
                    .padding(12.dp)
                    .clickable(actionStartActivity(openAppIntent)),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.Start,
            ) {
                if (hero != null) {
                    Text(
                        text = hero.itemName,
                        style = TextStyle(
                            color = ColorProvider(WidgetTextPrimary),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        ),
                        maxLines = 1,
                    )
                    Text(
                        text = "${hero.carrier.displayName} · ${statusLabel(hero.status, strings)}",
                        style = TextStyle(
                            color = ColorProvider(WidgetTextSecondary),
                            fontSize = 12.sp,
                        ),
                        maxLines = 1,
                    )
                    Text(
                        text = "${(hero.progress * 100).toInt()}%",
                        style = TextStyle(
                            color = ColorProvider(WidgetBrand),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                        ),
                    )
                } else {
                    Text(
                        text = strings.widgetNoActiveParcel,
                        style = TextStyle(
                            color = ColorProvider(WidgetTextPrimary),
                            fontSize = 13.sp,
                        ),
                    )
                }
            }
        }
    }
}
