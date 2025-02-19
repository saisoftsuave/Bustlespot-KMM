package org.softsuave.bustlespot.data.network.models.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllTasks(
    val taskDetails: List<TaskData>
)