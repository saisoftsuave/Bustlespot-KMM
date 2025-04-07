package org.softsuave.bustlespot.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllProjects(
    @SerialName("projectLists")
    val projectLists: List<Project>? = emptyList()
)