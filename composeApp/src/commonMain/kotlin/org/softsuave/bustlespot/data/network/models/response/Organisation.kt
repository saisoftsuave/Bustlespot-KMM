package org.softsuave.bustlespot.data.network.models.response

import kotlinx.serialization.Serializable


@Serializable
data class Organisation(
    val name: String,
    val organisationId: Int,
    val image: String,
    val roleId: Int,
    val enableScreenshot: Int,
    val description: String,
    val role: String,
    val otherRoleIds: List<Int>
)