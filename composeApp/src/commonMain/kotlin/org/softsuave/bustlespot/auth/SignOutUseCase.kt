package org.softsuave.bustlespot.auth

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import org.softsuave.bustlespot.MainViewModel
import org.softsuave.bustlespot.auth.utils.Result
import org.softsuave.bustlespot.network.APIEndpoints
import org.softsuave.bustlespot.network.BASEURL
import org.softsuave.bustlespot.network.models.response.SignOutResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softsuave.bustlespot.SessionManager

class SignOutUseCase(
    private val httpClient: HttpClient,
    private val settings: ObservableSettings,
    private val sessionManager: SessionManager
){
    operator fun invoke(): Flow<Result<SignOutResponseDto>> = flow {
        sessionManager.clearSession()
        try {
            emit(Result.Loading)
            val response: HttpResponse = httpClient.post("$BASEURL${APIEndpoints.SIGNOUT}") {
                contentType(ContentType.Application.Json)
                bearerAuth(sessionManager.accessToken)
            }

            if (response.status == HttpStatusCode.OK) {
                val result: SignOutResponseDto = response.body() // Deserialize the response body
                emit(Result.Success(result))
                sessionManager.clearSession()
            } else {
                emit(Result.Error("Failed to sign out: ${response.status} ${response.body<Any>()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message))
        }
    }
}