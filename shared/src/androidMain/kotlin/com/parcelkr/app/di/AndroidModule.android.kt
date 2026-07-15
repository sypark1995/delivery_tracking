package com.parcelkr.app.di

import android.content.Context
import com.parcelkr.app.data.DriverFactory
import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.data.real.RealTrackingApi
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.AndroidCsvExporter
import com.parcelkr.app.domain.AndroidDialerLauncher
import com.parcelkr.app.domain.AndroidUrlLauncher
import com.parcelkr.app.domain.CsvExporter
import com.parcelkr.app.domain.DialerLauncher
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.UrlLauncher
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun androidModule(
    context: Context,
    trackerClientId: String,
    trackerClientSecret: String,
): Module = module {
    single { ParcelDb(DriverFactory(context).create()) }
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    single<TrackingApi> {
        if (trackerClientId.isNotBlank() && trackerClientSecret.isNotBlank()) {
            RealTrackingApi(get(), trackerClientId, trackerClientSecret)
        } else {
            FakeTrackingApi()
        }
    }
    single<DialerLauncher> { AndroidDialerLauncher(context) }
    single<UrlLauncher> { AndroidUrlLauncher(context) }
    single<CsvExporter> { AndroidCsvExporter(context) }
}

fun initKoin(context: Context, trackerClientId: String, trackerClientSecret: String) {
    startKoin { modules(appModule, androidModule(context, trackerClientId, trackerClientSecret)) }
}
