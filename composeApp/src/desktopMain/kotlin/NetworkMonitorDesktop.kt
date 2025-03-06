package org.softsuave.bustlespot

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softsuave.bustlespot.network.NetworkMonitor
import java.util.concurrent.atomic.AtomicBoolean

class NetworkMonitorDesktop : NetworkMonitor {
    private val client: HttpClient by lazy { HttpClient() }
    private val status = AtomicBoolean(false)

    override val isConnected: Flow<Boolean> = flow {
        while (true) {
            val newStatus = try {
                client.get("https://www.google.com").status.value == 200
            } catch (e: Exception) {
                false
            }

            if (status.getAndSet(newStatus) != newStatus) {
                emit(newStatus)
                println("Network status changed: $newStatus")
            }
            delay(5000)
        }
    }
}

