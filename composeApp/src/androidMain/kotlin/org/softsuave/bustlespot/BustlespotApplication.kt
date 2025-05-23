package org.softsuave.bustlespot

import android.app.Application
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.softsuave.bustlespot.di.initKoin
import org.softsuave.bustlespot.network.NetworkMonitorProvider

class BustlespotApplication : Application() {
    override fun onCreate() {
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground,
                showPushNotification = true,
            )
        )
        super.onCreate()
        NetworkMonitorProvider.init(context = this)
        initKoin {
            androidLogger()
            androidContext(this@BustlespotApplication)
        }

    }
}