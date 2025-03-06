package org.softsuave.bustlespot.data.local

import kotlinx.serialization.json.Json
import org.softsuave.bustlespot.data.network.models.response.Organisation
import org.softsuave.bustlespot.tracker.data.model.ActivityData

fun com.example.Organisation.toDomain(): Organisation {
    return Organisation(
        organisationId = this.organisationId.toInt(),
        name = this.name,
        imageUrl = this.imageUrl,
        roleId = this.roleId.toInt(),
        enableScreenshot = this.enableScreenshot.toInt(),
        description = this.description,
        role = this.role,
        otherRoleIds  = if(this.otherRoleIds.isNotEmpty()) Json.decodeFromString<List<Int>>(this.otherRoleIds) else emptyList()
    )
}

fun com.example.ActivityData.toDomain(): ActivityData {
    return ActivityData(
        taskId = this.taskId?.toInt(),
        projectId = this.projectId?.toInt(),
        startTime = this.startTime,
        endTime = this.endTime,
        mouseActivity = this.mouseActivity?.toInt(),
        keyboardActivity = this.keyboardActivity?.toInt(),
        totalActivity = this.totalActivity?.toInt(),
        billable = this.billable,
        notes = this.notes,
        orgId = this.organisationId?.toInt(),
        uri = this.uri,
        unTrackedTime = this.unTrackedTime
    )
}