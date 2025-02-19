package org.softsuave.bustlespot.tracker.data.model

import kotlinx.serialization.Serializable


@Serializable
data class GetProjectRequest(
    val organisationId: String
)
