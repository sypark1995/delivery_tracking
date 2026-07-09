package com.parcelkr.app.di

import android.content.Context
import com.parcelkr.app.data.DriverFactory
import com.parcelkr.app.db.ParcelDb
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun androidModule(context: Context): Module = module {
    single { ParcelDb(DriverFactory(context).create()) }
}

fun initKoin(context: Context) {
    startKoin { modules(appModule, androidModule(context)) }
}
