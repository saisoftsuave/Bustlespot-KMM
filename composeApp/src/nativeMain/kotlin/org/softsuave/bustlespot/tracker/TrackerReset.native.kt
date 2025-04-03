package org.softsuave.bustlespot.tracker
import platform.Foundation.*
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.dispatch_after
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_time

actual fun scheduleWork(performTask: () -> Unit) {
    val delayMillis = getMillisUntilNextRun()

    val queue = dispatch_queue_create("com.example.scheduler", null)
    val dispatchTime = dispatch_time(DISPATCH_TIME_NOW, delayMillis * 1_000_000) // Convert ms to ns

    dispatch_after(dispatchTime, queue) {
        performTask()
        scheduleWork(performTask) // Reschedule for next day
    }
}

fun getMillisUntilNextRun(): Long {
    val now = NSDate()
    val calendar = NSCalendar.currentCalendar
    val components = calendar.components(
        NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond, fromDate = now
    )

    val targetTime = calendar.dateBySettingHour(3, minute = 0, second = 0, ofDate = now, options = 0u)!!

    return if (now.timeIntervalSince1970 >= targetTime.timeIntervalSince1970) {
        // If 3:00 AM is already passed, schedule for next day
        val nextRun = calendar.dateByAddingUnit(NSCalendarUnitDay, value = 1, toDate = targetTime, options = 0u)!!
        (nextRun.timeIntervalSince1970 * 1000).toLong() - (now.timeIntervalSince1970 * 1000).toLong()
    } else {
        (targetTime.timeIntervalSince1970 * 1000).toLong() - (now.timeIntervalSince1970 * 1000).toLong()
    }
}