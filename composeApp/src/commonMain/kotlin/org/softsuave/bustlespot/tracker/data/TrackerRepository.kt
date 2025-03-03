package org.softsuave.bustlespot.tracker.data

import org.softsuave.bustlespot.data.network.models.response.GetAllProjects
import org.softsuave.bustlespot.data.network.models.response.GetAllTasks
import kotlinx.coroutines.flow.Flow
import org.softsuave.bustlespot.auth.utils.Result
import org.softsuave.bustlespot.data.network.models.request.ActivityDto
import org.softsuave.bustlespot.data.network.models.response.ActivityResponseDto
import org.softsuave.bustlespot.data.network.models.response.GetAllActivities
import org.softsuave.bustlespot.tracker.data.model.ActivityDataResponse
import org.softsuave.bustlespot.tracker.data.model.PostActivityRequest
import org.softsuave.bustlespot.tracker.ui.model.GetTasksRequest

interface TrackerRepository {
    fun getAllProjects(organisationId : String) : Flow<Result<GetAllProjects>>

    fun getAllTask(getTasksRequest: GetTasksRequest) : Flow<Result<GetAllTasks>>

    fun postUserActivity(postActivityRequest: PostActivityRequest): Flow<Result<ActivityDataResponse>>

    fun getAllActivities(taskId : String) :  Flow<Result<GetAllActivities>>
}