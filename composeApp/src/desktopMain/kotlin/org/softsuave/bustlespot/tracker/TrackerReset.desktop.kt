package org.softsuave.bustlespot.tracker

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.softsuave.bustlespot.Log
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Timer

@OptIn(DelicateCoroutinesApi::class)
actual fun scheduleWork(performTask: () -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val toDelay = getMillisUntilNextRun()
        delay(toDelay)
        withContext(Dispatchers.Main) {
            performTask()
        }
    }
}

fun getMillisUntilNextRun(): Long {
    val now = LocalDateTime.now()
    val nextRun = now.withHour(3).withMinute(0).withSecond(0).withNano(0)

    val nextRunMillis = if (now.isAfter(nextRun)) {
        nextRun.plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    } else {
        nextRun.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    Log.i(nextRunMillis.toString())
    Log.i((nextRunMillis - System.currentTimeMillis()).toString())
    return nextRunMillis - System.currentTimeMillis()
}