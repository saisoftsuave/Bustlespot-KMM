package org.softsuave.bustlespot.tracker.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.util.reflect.TypeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softsuave.bustlespot.SessionManager
import org.softsuave.bustlespot.auth.utils.Result
import org.softsuave.bustlespot.network.APIEndpoints.GETALLACTIVITIES
import org.softsuave.bustlespot.network.APIEndpoints.GETALLPROJECTS
import org.softsuave.bustlespot.network.APIEndpoints.GETALLTASKS
import org.softsuave.bustlespot.network.APIEndpoints.POSTACTIVITY
import org.softsuave.bustlespot.network.BASEURL
import org.softsuave.bustlespot.network.models.request.ActivityDto
import org.softsuave.bustlespot.network.models.response.ActivityResponseDto
import org.softsuave.bustlespot.network.models.response.GetAllActivities
import org.softsuave.bustlespot.network.models.response.GetAllProjects
import org.softsuave.bustlespot.network.models.response.GetAllTasks

class TrackerRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) : TrackerRepository {

    override fun getAllProjects(): Flow<Result<GetAllProjects>> {
        return flow {
            try {
                emit(Result.Loading)
                val response: HttpResponse = client.get("$BASEURL$GETALLPROJECTS") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(sessionManager.accessToken)
                }
                if (response.status == HttpStatusCode.OK) {
                    val data: GetAllProjects = response.body()
                    emit(Result.Success(data))
                } else {
                    emit(Result.Error(message = "Failed to fetch Projects: ${response.status}"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun getAllTask(): Flow<Result<GetAllTasks>> {
        return flow {
            try {
                emit(Result.Loading)
                val response: HttpResponse = client.get("$BASEURL$GETALLTASKS") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(sessionManager.accessToken)
                }
                if (response.status == HttpStatusCode.OK) {
                    val data: GetAllTasks = response.body()
                    emit(Result.Success(data))
                } else {
                    emit(Result.Error(message = "Failed to fetch Projects: ${response.status}"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun postUserActivity(activityDto: ActivityDto): Flow<Result<ActivityResponseDto>> {
        return flow {
            try {
                emit(Result.Loading)
                val response: HttpResponse = client.post("$BASEURL$POSTACTIVITY") {
                    contentType(ContentType.Application.Json)
                    setBody(activityDto, bodyType = TypeInfo(ActivityDto::class))
                    bearerAuth(sessionManager.accessToken)
                }
                if (response.status == HttpStatusCode.OK) {
                    val data: ActivityResponseDto = response.body()
                    emit(Result.Success(data))
                } else {
                    emit(Result.Error(message = "Failed to fetch Projects: ${response.status}"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun getAllActivities(taskId: String): Flow<Result<GetAllActivities>> {
        return flow {
            try {
                emit(Result.Loading)
                val response: HttpResponse = client.post("$BASEURL$GETALLACTIVITIES/$taskId") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(sessionManager.accessToken)
                }
                if (response.status == HttpStatusCode.OK) {
                    val data: GetAllActivities = response.body()
                    emit(Result.Success(data))
                } else {
                    emit(Result.Error(message = "Failed to fetch Projects: ${response.status}"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }
    }
}