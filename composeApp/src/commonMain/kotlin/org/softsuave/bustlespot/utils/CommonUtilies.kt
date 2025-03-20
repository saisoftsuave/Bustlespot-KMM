package org.softsuave.bustlespot.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

expect fun isAndroid(): Boolean


@Composable
expect fun handleBackPress(
    onBack: () -> Unit = {}
)


val BustleSpotRed : Color
    get() = Color(242, 60, 75, 255)