package org.softsuave.bustlespot.tracker.data

import com.example.Database
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.util.reflect.TypeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softsuave.bustlespot.Log
import org.softsuave.bustlespot.SessionManager
import org.softsuave.bustlespot.auth.signin.data.BaseResponse
import org.softsuave.bustlespot.auth.utils.Result
import org.softsuave.bustlespot.data.local.toDomain
import org.softsuave.bustlespot.data.network.APIEndpoints.GETALLACTIVITIES
import org.softsuave.bustlespot.data.network.APIEndpoints.GETALLPROJECTS
import org.softsuave.bustlespot.data.network.APIEndpoints.GETALLTASKS
import org.softsuave.bustlespot.data.network.APIEndpoints.POSTACTIVITY
import org.softsuave.bustlespot.data.network.BASEURL
import org.softsuave.bustlespot.data.network.models.response.ErrorResponse
import org.softsuave.bustlespot.data.network.models.response.GetAllActivities
import org.softsuave.bustlespot.data.network.models.response.GetAllProjects
import org.softsuave.bustlespot.data.network.models.response.GetAllTasks
import org.softsuave.bustlespot.tracker.data.model.ActivityDataResponse
import org.softsuave.bustlespot.tracker.data.model.GetProjectRequest
import org.softsuave.bustlespot.tracker.data.model.PostActivityRequest
import org.softsuave.bustlespot.tracker.ui.model.GetTasksRequest

class TrackerRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager,
    private val db : Database,
) : TrackerRepository {

    override fun getAllProjects(organisationId: String): Flow<Result<GetAllProjects>> {
        return flow {
            try {
                emit(Result.Loading)
                val response: HttpResponse = client.post("$BASEURL$GETALLPROJECTS") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        GetProjectRequest(organisationId)
                    )
                    bearerAuth(sessionManager.accessToken)
                }
                if (response.status == HttpStatusCode.OK) {
                    val data: BaseResponse<GetAllProjects> = response.body()
                    println(data)
                    emit(Result.Success(data.data ?: GetAllProjects(emptyList())))
                } else {
                    val responseBody: BaseResponse<ErrorResponse> = response.body()
                    println(responseBody)
                    emit(Result.Error(message = "Failed to fetch Projects: ${response.status}"))
                    println("Failed to fetch Projects: ${response}")
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun getAllTask(getTasksRequest: GetTasksRequest): Flow<Result<GetAllTasks>> {
        return flow {
            try {
                emit(Result.Loading)
                val response: HttpResponse = client.post("$BASEURL$GETALLTASKS") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        getTasksRequest
                    )
                    bearerAuth(sessionManager.accessToken)
                }
                if (response.status == HttpStatusCode.OK) {
                    val data: BaseResponse<GetAllTasks> = response.body()
                    println(data)
                    emit(Result.Success(data.data ?: GetAllTasks(emptyList())))
                } else {
                    emit(Result.Error(message = "Failed to fetch Projects: ${response.status}"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun postUserActivity(postActivityRequest: PostActivityRequest): Flow<Result<ActivityDataResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response: HttpResponse = client.post("$BASEURL$POSTACTIVITY") {
                    contentType(ContentType.Application.Json)
                    setBody(postActivityRequest, bodyType = TypeInfo(PostActivityRequest::class))
                    bearerAuth(sessionManager.accessToken)
                }
                if (response.status == HttpStatusCode.OK) {
                    val data: ActivityDataResponse = response.body()
                    emit(Result.Success(data))
                } else {
                    Log.d("postActivity --> failed")
                    saveFailedPostUserActivity(postActivityRequest)
                    emit(Result.Error(message = "Failed post activity: ${response.status}"))
                }
            } catch (e: Exception) {
                Log.d("postActivity --> failed")
                saveFailedPostUserActivity(postActivityRequest)
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
    
    private fun saveFailedPostUserActivity(postActivityRequest: PostActivityRequest) {
        postActivityRequest.activityData.forEach { activityData ->
            db.transaction {
                db.activitiesDatabaseQueries.insertActivity(
                    taskId = activityData.taskId?.toLong(),
                    projectId = activityData.projectId?.toLong(),
                    startTime = activityData.startTime,
                    endTime = activityData.endTime,
                    mouseActivity = activityData.mouseActivity?.toLong(),
                    keyboardActivity = activityData.keyboardActivity?.toLong(),
                    totalActivity = activityData.totalActivity?.toLong(),
                    billable = activityData.billable,
                    notes = activityData.notes,
                    organisationId = activityData.orgId?.toLong(),
                    uri = activityData.uri,
                    unTrackedTime = activityData.unTrackedTime
                )
            }
        }
        Log.d("call saved to db")
    }

   override suspend fun checkLocalDbAndPostActivity(){
       Log.d("post local failed call is started")
        val localData = db.activitiesDatabaseQueries.getAllActivities().executeAsList().map { it.toDomain() }
        if(localData.isNotEmpty()){
            val postActivityRequest = PostActivityRequest(localData.toMutableList())
            postUserActivity(postActivityRequest).collect{ result ->
                when(result){
                    is Result.Error ->  {
                        Log.d("failed to post from local db")
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        Log.d("Success to post from local db")
                        db.activitiesDatabaseQueries.deleteAllActivities()
                    }
                }
            }
        }
    }
}