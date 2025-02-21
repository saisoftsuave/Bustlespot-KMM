package org.softsuave.bustlespot.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import kotlinx.datetime.Instant


@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        imageLoader = ImageLoader(LocalPlatformContext.current)
    )
}
fun convertIsoToLocalSeconds(isoTime: String): Long {
    val instant = Instant.parse(isoTime)
    val localDateTime = instant.epochSeconds
    return localDateTime
}

fun main() {
    val startTime = "2025-02-20T10:10:08.000Z"
    val endTime = "2025-02-20T10:20:08.000Z"

    val startSeconds = convertIsoToLocalSeconds(startTime)
    val endSeconds = convertIsoToLocalSeconds(endTime)

    println("Start seconds: $startSeconds")
    println("End seconds: ${endSeconds-startSeconds}")
}
