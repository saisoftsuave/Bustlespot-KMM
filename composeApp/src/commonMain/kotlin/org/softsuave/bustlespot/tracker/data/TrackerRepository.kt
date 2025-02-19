package org.softsuave.bustlespot.tracker.data

import org.softsuave.bustlespot.data.network.models.response.GetAllProjects
import org.softsuave.bustlespot.data.network.models.response.GetAllTasks
import kotlinx.coroutines.flow.Flow
import org.softsuave.bustlespot.auth.utils.Result
import org.softsuave.bustlespot.data.network.models.request.ActivityDto
import org.softsuave.bustlespot.data.network.models.response.ActivityResponseDto
import org.softsuave.bustlespot.data.network.models.response.GetAllActivities

interface TrackerRepository {
    fun getAllProjects() : Flow<Result<GetAllProjects>>

    fun getAllTask() : Flow<Result<GetAllTasks>>

    fun postUserActivity(activityDto: ActivityDto): Flow<Result<ActivityResponseDto>>

    fun getAllActivities(taskId : String) :  Flow<Result<GetAllActivities>>
}