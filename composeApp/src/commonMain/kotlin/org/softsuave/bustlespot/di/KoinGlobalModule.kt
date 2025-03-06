package org.softsuave.bustlespot.di

import com.russhwolf.settings.ObservableSettings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.softsuave.bustlespot.MainViewModel
import org.softsuave.bustlespot.SessionManager
import org.softsuave.bustlespot.auth.signin.data.AccessTokenResponse
import org.softsuave.bustlespot.createSettings
import org.softsuave.bustlespot.getEngine
import org.softsuave.bustlespot.data.network.BASEURL
import org.softsuave.bustlespot.tracker.di.trackerModule
import org.koin.dsl.module
import org.softsuave.bustlespot.data.local.createDriver
import com.example.Database
import org.softsuave.bustlespot.network.NetworkMonitor
import org.softsuave.bustlespot.network.NetworkMonitorProvider

val koinGlobalModule = module {
    single { MainViewModel(get()) { provideUnauthenticatedHttpClient() } }
    factory { provideHttpClient(get(), get()) }
    trackerModule
    single<ObservableSettings> {
        createSettings()
    }
    single<Database> {
        provideSqlDelightDatabase()
    }
    single<NetworkMonitor> {
        provideNetworkMonitorInstance()
    }
}

fun provideUnauthenticatedHttpClient(): HttpClient {
    return HttpClient(getEngine()) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
}

fun provideHttpClient(settings: ObservableSettings, sessionManager: SessionManager): HttpClient {
    return HttpClient(getEngine()) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }


        install(HttpCallValidator) {
            validateResponse { response ->
                if (response.status.value == 401) {
                    println("Received 401: Forcing logout")
                    sessionManager.clearSession()
                }
            }
        }

        if (settings.getString("access_token", "").isNotEmpty()) {
            install(Auth) {
                bearer {
//                    loadTokens {
//                        BearerTokens(
//                            accessToken =settings.getString("access_token",""),
//                            refreshToken = sessionManager.accessToken
//                        )
//                    }

                    refreshTokens {
                        try {
                            val response: HttpResponse = client.get("$BASEURL/auth/refresh-token") {
                                bearerAuth(sessionManager.accessToken)
                            }

                            if (response.status.isSuccess()) {
                                val newAccessToken =
                                    response.body<AccessTokenResponse>().access_token
                                settings.putString("access_token", newAccessToken)
                                sessionManager.updateAccessToken(newAccessToken)

                                return@refreshTokens BearerTokens(
                                    newAccessToken,
                                    sessionManager.accessToken
                                )
                            }
                        } catch (e: Exception) {
                            println("Token refresh failed: ${e.message}")
                        }
                        return@refreshTokens null
                    }
                }
            }
        }
    }
}

fun provideSqlDelightDatabase(): Database {
    return Database.invoke(createDriver())
}


fun provideNetworkMonitorInstance() : NetworkMonitor {
    return NetworkMonitorProvider.getInstance()
}

//fun provideRealmeDatabase(): Realm {
//    val config = RealmConfiguration.Builder(
//        schema = setOf(
//            OrganisationObj::class
//        )
//    ).compactOnLaunch()
//        .build()
//    return Realm.open(config)
//}