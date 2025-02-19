package org.softsuave.bustlespot.data.network.models.response

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val projectId: Int,
    val name: String,
    val status: Int,
    val startDate: String,
    val userId: Int,
    val roleId: Int
) : DisplayItem()

open class DisplayItem {

}
