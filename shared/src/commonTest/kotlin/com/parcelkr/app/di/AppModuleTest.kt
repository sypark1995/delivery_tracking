package com.parcelkr.app.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.data.fake.FakeTrackingApi
import com.parcelkr.app.db.ParcelDb
import com.parcelkr.app.domain.CarrierDetector
import com.parcelkr.app.domain.TrackingApi
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class AppModuleTest {
    @AfterTest fun tearDown() = stopKoin()

    @Test fun graph_resolves_core_dependencies() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParcelDb.Schema.create(driver)
        val testDb = module { single { ParcelDb(driver) } }
        val testTrackingApi = module { single<TrackingApi> { FakeTrackingApi() } }
        val koin = startKoin { modules(appModule, testDb, testTrackingApi) }.koin
        assertNotNull(koin.get<TrackingApi>())
        assertNotNull(koin.get<CarrierDetector>())
        assertNotNull(koin.get<ParcelRepository>())
    }
}
