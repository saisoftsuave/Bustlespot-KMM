package org.softsuave.bustlespot.data.network.models.response



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("error_code")
    val errorCode: String? = null,
    @SerialName("message")
    val message: String? = null
)