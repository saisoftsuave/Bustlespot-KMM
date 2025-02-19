package org.softsuave.bustlespot.auth.signin.data

import kotlinx.serialization.Serializable



@Serializable
data class User(
    val firstName: String ="",
    val lastName: String="",
    val email: String="",
    val token: String="",
)