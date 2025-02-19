package org.softsuave.bustlespot.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllOrganisations(
    @SerialName("organisationList")
    val listOfOrganisations: List<Organisation>,
)