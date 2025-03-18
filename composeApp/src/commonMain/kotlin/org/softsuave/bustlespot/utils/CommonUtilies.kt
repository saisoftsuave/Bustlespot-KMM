package org.softsuave.bustlespot.utils

import androidx.compose.runtime.Composable

expect fun isAndroid() : Boolean


@Composable
expect fun handleBackPress(
    onBack: () -> Unit = {}
)