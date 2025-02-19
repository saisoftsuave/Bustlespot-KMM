package org.softsuave.bustlespot.data.local

import org.softsuave.bustlespot.data.local.realme.objects.OrganisationObj
import org.softsuave.bustlespot.data.network.models.response.Organisation

fun OrganisationObj.toLocal(): Organisation {
    return Organisation(
        name,
        organisationId,
        image,
        roleId,
        enableScreenshot,
        description,
        role,
        otherRoleIds
    )
}