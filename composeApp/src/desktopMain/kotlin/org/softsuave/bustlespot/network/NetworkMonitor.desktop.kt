package org.softsuave.bustlespot.network

import NetworkMonitorDesktop

actual object NetworkMonitorProvider {
    actual fun getInstance(): NetworkMonitor = NetworkMonitorDesktop()
}