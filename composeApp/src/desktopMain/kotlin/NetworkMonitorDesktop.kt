package org.softsuave.bustlespot

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softsuave.bustlespot.network.NetworkMonitor


class NetworkMonitorDesktop() : NetworkMonitor {
    private val client = HttpClient()

    override val isConnected: Flow<Boolean> = flow {
        while (true) {
            try {
                val response: HttpResponse = client.get("https://www.google.com")
                emit(response.status.value == 200)
            } catch (e: Exception) {
                emit(false)
            }
            delay(5000)
        }
    }

}
