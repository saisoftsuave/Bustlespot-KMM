package org.softsuave.bustlespot.network

import kotlinx.coroutines.flow.Flow

expect object NetworkMonitorProvider{
    fun getInstance() : NetworkMonitor
}

interface NetworkMonitor {
    val isConnected: Flow<Boolean>
}