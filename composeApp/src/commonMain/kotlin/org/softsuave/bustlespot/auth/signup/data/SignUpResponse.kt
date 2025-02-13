package org.softsuave.bustlespot.auth.signup.data

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val message: String
)