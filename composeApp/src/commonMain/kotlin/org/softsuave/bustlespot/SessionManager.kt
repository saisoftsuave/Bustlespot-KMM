package org.softsuave.bustlespot

import com.russhwolf.settings.ObservableSettings
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import org.softsuave.bustlespot.data.local.realme.objects.OrganisationObj


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

    suspend fun clearSession() {
        isLoggedIn.value = false
        settings.remove("access_token")
//        resetRealm(realmDb,realmDb.configuration as RealmConfiguration)
        Log.d("Session cleared. isLoggedIn = $isLoggedIn")
    }
//    suspend fun resetRealm(realm: Realm, config: RealmConfiguration) {
//        realm.close()   // Close Realm instance
//        Realm.deleteRealm(config)  // Delete Realm database
//        println("Realm database deleted!")
//    }
    init {
        isLoggedIn = MutableStateFlow(settings.getString("access_token", "").isNotEmpty())
    }
}
