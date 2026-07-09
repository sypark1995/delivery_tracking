package com.parcelkr.app

import android.app.Application
import com.parcelkr.app.di.initKoin

class ParcelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}
