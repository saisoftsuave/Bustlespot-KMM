package org.softsuave.bustlespot.auth.signin.data

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val email: String,
    val password: String
)