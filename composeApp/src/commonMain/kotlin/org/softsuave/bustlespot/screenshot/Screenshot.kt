package org.softsuave.bustlespot.screenshot

import androidx.compose.ui.graphics.ImageBitmap

data class Screenshot(
    val screenshot: ImageBitmap?,
    val screenshotTakenTime: Long
)
