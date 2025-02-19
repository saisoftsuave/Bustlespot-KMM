package org.softsuave.bustlespot.data.network.models.response

import kotlinx.serialization.Serializable

@Serializable
data class GetAllProjects(
    val projectLists: List<Project>?
)