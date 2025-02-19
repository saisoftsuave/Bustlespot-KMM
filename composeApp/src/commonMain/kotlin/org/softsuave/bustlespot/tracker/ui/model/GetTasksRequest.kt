package org.softsuave.bustlespot.tracker.ui.model

import kotlinx.serialization.Serializable


@Serializable
data class GetTasksRequest(
    val projectId: String, val organisationId: String
)
