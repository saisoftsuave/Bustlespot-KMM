package org.softsuave.bustlespot.timer

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import platform.Foundation.NSTimer

import kotlin.random.Random

actual class TrackerModule actual constructor(private val viewModelScope: CoroutineScope) {
    actual var trackerTime: MutableStateFlow<Int> = MutableStateFlow(0)
    actual var isTrackerRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    actual var isIdealTimerRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    actual var idealTime: MutableStateFlow<Int> = MutableStateFlow(0)
    actual var screenShotTakenTime: MutableStateFlow<Int> = MutableStateFlow(0)
    actual var keyboradKeyEvents: MutableStateFlow<Int> = MutableStateFlow(0)
    actual var mouseKeyEvents: MutableStateFlow<Int> = MutableStateFlow(0)
    actual var mouseMotionCount: MutableStateFlow<Int> = MutableStateFlow(0)
    actual var customeTimeForIdleTime: MutableStateFlow<Int> = MutableStateFlow(480)
    actual var numberOfScreenshot: MutableStateFlow<Int> = MutableStateFlow(1)

//    private val globalEventListener: GlobalEventListener = GlobalEventListener()
    private val screenShot = MutableStateFlow<ImageBitmap?>(null)
    actual val screenShotState: StateFlow<ImageBitmap?> = screenShot
    private val randomTime: MutableStateFlow<List<Int>> = MutableStateFlow(emptyList())

    private var trackerTimer: NSTimer? = null
    private var idleTimer: NSTimer? = null
    private var trackerIndex = 0
    private val screenShotFrequency = 1
    private val screenshotLimit = 1

    actual fun resetTimer() {
        isTrackerRunning.value = false
        idealTime.value = 0
        trackerTime.value = 0
    }

    actual fun stopTimer() {
        isTrackerRunning.value = false
//        globalEventListener.unregisterListeners()
    }

    actual fun resumeTracker() {
        isTrackerRunning.value = true
//        globalEventListener.registerListeners()
    }

    private fun setRandomTimes(
        randomTimes: MutableStateFlow<List<Int>>,
        overallStart: Int,
        overallEnd: Int,
        numberOfIntervals: Int = 10
    ) {
        val totalDuration = overallEnd - overallStart
        if (totalDuration % numberOfIntervals != 0) {
            throw IllegalArgumentException("Interval length ($totalDuration) must be evenly divisible by $numberOfIntervals.")
        }
        val intervalSize = totalDuration / numberOfIntervals
        randomTimes.value = List(numberOfIntervals) { i ->
            val subIntervalStart = overallStart + i * intervalSize
            val subIntervalEnd = overallStart + (i + 1) * intervalSize
            Random.nextInt(from = subIntervalStart, until = subIntervalEnd)
        }
    }

    actual fun startTimer() {
        isTrackerRunning.value = true
//        globalEventListener.registerListeners()
        setRandomTimes(randomTime, 0, screenshotLimit * 60, screenShotFrequency)
        trackerIndex = 0

        viewModelScope.launch {
//            globalEventListener.fKeyCount.collectLatest { count ->
//                keyboradKeyEvents.emit(count)
//                idealTime.value = 0
//            }
        }
        viewModelScope.launch {
//            globalEventListener.fMouseCount.collectLatest { count ->
//                mouseKeyEvents.emit(count)
//                idealTime.value = 0
//            }
        }
        viewModelScope.launch {
//            globalEventListener.fMouseMotionCount.collectLatest { count ->
//                mouseMotionCount.emit(count)
//                idealTime.value = 0
//            }
        }

        trackerTimer = NSTimer.scheduledTimerWithTimeInterval(1.0, true) {
            if (isTrackerRunning.value) {
                trackerTime.value++
                if (trackerTime.value % 60 == 0) {
                    screenShotTakenTime.value++
                }
                if (trackerIndex < randomTime.value.size && trackerTime.value > randomTime.value[trackerIndex]) {
                    takeScreenShot()
                    trackerIndex++
                    if (trackerIndex == randomTime.value.size) {
                        setRandomTimes(randomTime, trackerTime.value, trackerTime.value + (screenshotLimit * 60), screenShotFrequency)
                        trackerIndex = 0
                    }
                }
            }
        }
    }

    actual fun resetIdleTimer() {
        isIdealTimerRunning.value = false
        idealTime.value = 0
    }

    actual fun stopIdleTimer() {
        isIdealTimerRunning.value = false
    }

    actual fun startIdleTimerClock() {
        isIdealTimerRunning.value = true
    }

    fun takeScreenShot() {
        screenShot.value = org.softsuave.bustlespot.screenshot.takeScreenShot()
        screenShotTakenTime.value = 0
    }

    actual fun updateTrackerTimer() {
        val newTime = trackerTime.value - customeTimeForIdleTime.value
        trackerTime.value = if (newTime < 0) 0 else newTime
    }

    actual fun startIdleTimer() {
        idleTimer = NSTimer.scheduledTimerWithTimeInterval(1.0, true) {
            idealTime.value += 1
        }
    }

    actual fun addCustomTimeForIdleTime(time: Int) {
        customeTimeForIdleTime.value = time
    }

    actual fun stopScreenshotTask() {
    }

    actual fun startScreenshotTask() {
    }

    actual fun pauseScreenshotTask() {
    }

    actual fun resumeScreenshotTask() {
    }
}
