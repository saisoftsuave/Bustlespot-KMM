package org.softsuave.bustlespot.tracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostActivityRequest(
    @SerialName("activityData") var activityData: MutableList<ActivityData> = arrayListOf()
)


@Serializable
data class ActivityData(
    @SerialName("taskId") var taskId: Int? = 0,
    @SerialName("projectId") var projectId: Int? = 0,
    @SerialName("startTime") var startTime: String? = null,
    @SerialName("endTime") var endTime: String? = null,
    @SerialName("mouseActivity") var mouseActivity: Int? = 0,
    @SerialName("keyboardActivity") var keyboardActivity: Int? = 0,
    @SerialName("totalActivity") var totalActivity: Int? = 0,
    @SerialName("billable") var billable: String? = "",
    @SerialName("notes") var notes: String? = null,
    @SerialName("organisationId") var orgId: Int? = 0,
    @SerialName("uri") var uri: String? = null,
    @SerialName("unTrackedTime") var unTrackedTime: Long? = null
)