package org.softsuave.bustlespot

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.softsuave.bustlespot.di.initKoin

class BustlespotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@BustlespotApplication)
        }
    }
}