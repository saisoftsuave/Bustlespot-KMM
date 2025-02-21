package org.softsuave.bustlespot.auth.utils

fun secondsToTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    val hoursStr = if (hours < 10) "0$hours" else "$hours"
    val minutesStr = if (minutes < 10) "0$minutes" else "$minutes"
    val secsStr = if (secs < 10) "0$secs" else "$secs"

    return "$hoursStr:$minutesStr:$secsStr"
}


fun secondsToTimeForScreenshot(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    return if (minutes == 0) {
        "less than a minute ago"
    } else if (minutes < 60) {
        "$minutes minutes ago"
    } else {
        if (hours != 0) "about $hours hours ago" else
            "about $hours hours $minutes mintues ago"
    }
}


fun secondsToTimeFormat(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    val hoursStr = if (hours < 10) "0$hours" else "$hours"
    val minutesStr = if (minutes < 10) "0$minutes" else "$minutes"
    val secsStr = if (secs < 10) "0$secs" else "$secs"
    return "${hoursStr}hrs ${minutesStr}min"
}

fun String.timeStringToSeconds(): Int {
    val parts = this.split(":")
    if (parts.size != 3) throw IllegalArgumentException("Time must be in HH:MM:SS format")
    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()
    val seconds = parts[2].toInt()
    return hours * 3600 + minutes * 60 + seconds
}