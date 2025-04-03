package org.softsuave.bustlespot.tracker

expect fun scheduleWork(
    performTask:() -> Unit ={}
)