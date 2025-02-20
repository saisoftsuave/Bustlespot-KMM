package org.softsuave.bustlespot.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskData(
    @SerialName("taskId") val taskId: Int? = 0,
    @SerialName("name") val name: String,
    @SerialName("projectId") val projectId: Int = 0,
    @SerialName("projectName") val projectName: String? = null,
    @SerialName("organisationId") val organisationId: Int? = 0,
    @SerialName("time") var time: Int? = 0,
    @SerialName("startTime") val startTime: String? = null,
    @SerialName("endTime") val endTime: String? = null,
    @SerialName("notes") var notes: String? = null,
    @SerialName("lastScreenShotTime") val lastScreenShotTime: String? = null,
    @SerialName("screenshots") val screenshots: String? = null,
    @SerialName("unTrackedTime") var unTrackedTime: Int? = null
) : DisplayItem()