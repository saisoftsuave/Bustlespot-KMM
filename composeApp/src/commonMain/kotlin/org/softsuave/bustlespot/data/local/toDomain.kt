package org.softsuave.bustlespot.data.local

import kotlinx.serialization.json.Json
import org.softsuave.bustlespot.data.network.models.response.Organisation

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