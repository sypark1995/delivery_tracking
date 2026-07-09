package com.parcelkr.app.di

import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.data.fake.FakeCarrierDetector
import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.CarrierDetector
import com.parcelkr.app.domain.TrackingApi
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    single<TrackingApi> { FakeTrackingApi() }
    single<CarrierDetector> { FakeCarrierDetector() }
    single { ParcelRepository(get<ParcelDb>()) }
}
