package org.softsuave.bustlespot


import kotlinx.coroutines.flow.Flow
import org.softsuave.bustlespot.network.NetworkMonitor

class NetworkMonitorIos() : NetworkMonitor {
    override val isConnected: Flow<Boolean>
        get() = TODO("Not yet implemented")
//    private val _isConnected = MutableStateFlow(true)
//    override val isConnected: Flow<Boolean> = _isConnected
//
//    private val monitor = nw_path_monitor_create()
//
//    init {
//        nw_path_monitor_set_update_handler(monitor) { path ->
//            _isConnected.value = nw_path_get_status(path) == nw_path_status_satisfied
//        }
//        nw_path_monitor_start(monitor)
//    }
}