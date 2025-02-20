package org.softsuave.bustlespot.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName("projectId")
    val projectId: Int,
    val name: String,
    val status: Int,
    val startDate: String,
    val userId: Int? = null,
    @SerialName("roleId")
    val roleId: Int? = null
) : DisplayItem()

open class DisplayItem {

}
