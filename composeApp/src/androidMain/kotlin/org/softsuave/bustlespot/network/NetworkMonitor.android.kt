package org.softsuave.bustlespot.network

import android.content.Context
import org.softsuave.bustlespot.NetworkMonitorAndroid

actual object NetworkMonitorProvider {
    private lateinit var instance: NetworkMonitor

    fun init(context: Context) {
        if (!::instance.isInitialized) {
            instance = NetworkMonitorAndroid(context)
        }
    }

    actual fun getInstance(): NetworkMonitor {
        check(::instance.isInitialized) { "NetworkMonitorProvider.init(context) must be called first!" }
        return instance
    }
}