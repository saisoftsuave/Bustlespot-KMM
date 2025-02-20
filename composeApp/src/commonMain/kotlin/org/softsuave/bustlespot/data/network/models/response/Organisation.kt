package org.softsuave.bustlespot.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Organisation(
    val name: String,
    val organisationId: Int,
    @SerialName("image")
    val imageUrl: String,
    val roleId: Int,
    val enableScreenshot: Int,
    val description: String,
    val role: String,
    val otherRoleIds: List<Int>
)