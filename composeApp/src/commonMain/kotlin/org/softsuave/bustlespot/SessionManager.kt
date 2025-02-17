package org.softsuave.bustlespot

import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow


class SessionManager(private val settings: ObservableSettings) {
    var isLoggedIn = MutableStateFlow(false)
        private set

    val flowAccessToken: Flow<String> = callbackFlow {
        val listener = settings.addStringListener(
            key = "access_token",
            defaultValue = "",
            callback = { token ->
                trySend(token)
            }
        )

        awaitClose {
            listener.deactivate()
        }
    }
    var accessToken = settings.getString("access_token", "")

    fun setToken(token: String) {
        accessToken = token
    }

    fun updateAccessToken(token: String): Boolean {
        isLoggedIn.value = true
        settings.putString("access_token", token)
        println("Updated access token. isLoggedIn = $isLoggedIn")
        return true
    }

    fun clearSession() {
        isLoggedIn.value = false
        settings.remove("access_token")
        println("Session cleared. isLoggedIn = $isLoggedIn")
    }

    init {
        isLoggedIn = MutableStateFlow(settings.getString("access_token", "").isNotEmpty())
    }
}
