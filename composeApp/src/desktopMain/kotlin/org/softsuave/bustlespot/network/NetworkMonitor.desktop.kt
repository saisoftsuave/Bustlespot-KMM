package org.softsuave.bustlespot.network

import org.softsuave.bustlespot.NetworkMonitorDesktop

actual object NetworkMonitorProvider {
    actual fun getInstance(): NetworkMonitor = NetworkMonitorDesktop()
}