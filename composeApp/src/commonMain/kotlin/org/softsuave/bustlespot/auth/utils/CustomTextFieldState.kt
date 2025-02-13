package org.softsuave.bustlespot.auth.utils

data class CustomTextFieldState(
    val value: String = "",
    val error: String = "",
    val isValid: Boolean = false,
)