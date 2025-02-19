package org.softsuave.bustlespot.data.network.models.response

import kotlinx.serialization.Serializable

@Serializable
data class TaskData(
    val taskId: Int,
    val name: String,
    val projectId: Int,
    val projectName: String,
    val organisationId: Int,
    val time: Int? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val screenshots: String,
    val notes: String,
    val unTrackedTime: Int? = null,
    val lastScreenShotTime: String,
    val totalTime: Int
) : DisplayItem()