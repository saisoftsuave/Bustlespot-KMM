package org.softsuave.bustlespot.network

import org.softsuave.bustlespot.NetworkMonitorIos

actual object NetworkMonitorProvider {
    actual fun getInstance(): NetworkMonitor = NetworkMonitorIos()
}