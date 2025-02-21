package org.softsuave.bustlespot.tracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ActivityDataResponse(
    @SerialName("success") var success: Boolean? = null,
    @SerialName("activities") var activities: Activities? = Activities()
)


@Serializable
data class Activities(
    @SerialName("fieldCount") var fieldCount: Int? = 0,
    @SerialName("affectedRows") var affectedRows: Int? = 0,
    @SerialName("insertId") var insertId: Int? = 0,
    @SerialName("serverStatus") var serverStatus: Int? = 0,
    @SerialName("warningCount") var warningCount: Int? = 0,
    @SerialName("message") var message: String? = null,
    @SerialName("protocol41") var protocol41: Boolean? = null,
    @SerialName("changedRows") var changedRows: Int? = 0
)