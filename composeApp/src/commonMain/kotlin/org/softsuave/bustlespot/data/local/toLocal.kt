package org.softsuave.bustlespot.data.local

import org.softsuave.bustlespot.data.local.realme.objects.OrganisationObj
import org.softsuave.bustlespot.data.network.models.response.Organisation

fun OrganisationObj.toLocal() : Organisation{
    return Organisation(
        organisationId = this.organisationId,
        organisationName = this.organisationName,
        organisationDescription = this.organisationDescription,
        isDeleted = this.isDeleted
    )
}