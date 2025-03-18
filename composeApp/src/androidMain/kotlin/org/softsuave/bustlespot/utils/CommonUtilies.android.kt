package org.softsuave.bustlespot.utils

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable


actual fun isAndroid(): Boolean = true

@Composable
actual fun handleBackPress(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
}