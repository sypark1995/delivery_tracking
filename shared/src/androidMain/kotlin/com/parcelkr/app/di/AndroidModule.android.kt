package com.parcelkr.app.di

import android.content.Context
import com.parcelkr.app.data.DriverFactory
import com.parcelkr.app.data.ForwardingParcelRepository
import com.parcelkr.app.data.fake.FakeOverseasTrackingApi
import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.data.real.RealTrackingApi
import com.parcelkr.app.data.real.Track17Api
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.AndroidCsvExporter
import com.parcelkr.app.domain.AndroidDialerLauncher
import com.parcelkr.app.domain.AndroidUrlLauncher
import com.parcelkr.app.domain.CsvExporter
import com.parcelkr.app.domain.DialerLauncher
import com.parcelkr.app.domain.OverseasTrackingApi
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
    track17ApiKey: String = "",
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
    single { ForwardingParcelRepository(get()) }
    single<OverseasTrackingApi> {
        if (track17ApiKey.isNotBlank()) {
            Track17Api(get(), track17ApiKey)
        } else {
            FakeOverseasTrackingApi()
        }
    }
}

fun initKoin(context: Context, trackerClientId: String, trackerClientSecret: String, track17ApiKey: String = "") {
    startKoin { modules(appModule, androidModule(context, trackerClientId, trackerClientSecret, track17ApiKey)) }
}
