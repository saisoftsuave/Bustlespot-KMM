package org.softsuave.bustlespot.timer

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.softsuave.bustlespot.Log
import org.softsuave.bustlespot.accessability.GlobalAccessibilityEvents
import org.softsuave.bustlespot.tracker.data.model.ActivityData
import org.softsuave.bustlespot.tracker.data.model.PostActivityRequest
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

actual class TrackerModule actual constructor(
    private val viewModelScope: CoroutineScope
) {
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
    actual var isTrackerStarted: MutableStateFlow<Boolean> = MutableStateFlow(false)


    private var timer = Timer()
    private var isTaskScheduled = AtomicBoolean(false)
    private var isIdleTaskScheduled = AtomicBoolean(false)

    private val screenShot = MutableStateFlow<ImageBitmap?>(null)
    actual val screenShotState: StateFlow<ImageBitmap?> = screenShot
    private val randomTime: MutableStateFlow<List<Int>> = MutableStateFlow(emptyList())
    private var screenshotRepeatingTask: TimerTask? = null
    private var screenshotOneShotTask: TimerTask? = null

    @Volatile
    private var isPaused = false

    private var trackerTimerTask: TimerTask? = null
    private var idleTimerTask: TimerTask? = null
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
        //   globalEventListener.unregisterListeners()
    }

    actual fun resumeTracker() {
        isTrackerRunning.value = true
        //  globalEventListener.registerListeners()
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
        //   globalEventListener.registerListeners()
        setRandomTimes(
            randomTime,
            overallStart = 0,
            overallEnd = screenshotLimit * 60,
            numberOfIntervals = screenShotFrequency
        )
        trackerIndex = 0
        // Collect key events
        viewModelScope.launch {
            GlobalAccessibilityEvents.keyCountFlow.collectLatest { count ->
                keyboradKeyEvents.emit(count)
                idealTime.value = 0
                Log.d("TrackerModule Key Count: $count")
            }
        }

        // Collect click events
        viewModelScope.launch {
            GlobalAccessibilityEvents.mouseCountFlow.collectLatest { count ->
                mouseKeyEvents.emit(count)
                idealTime.value = 0
                Log.d("TrackerModule Mouse Click Count: $count")
            }
        }

        // Collect mouse motion events
        viewModelScope.launch {
            GlobalAccessibilityEvents.mouseMotionCountFlow.collectLatest { count ->
                mouseMotionCount.emit(count)
                idealTime.value = 0
                Log.d("TrackerModule Mouse Motion Count: $count")
            }
        }
        if (!isIdleTaskScheduled.getAndSet(true)) {
            idleTimerTask = object : TimerTask() {
                override fun run() {
                    idealTime.value += 1
                }
            }
            timer.scheduleAtFixedRate(idleTimerTask, 1000, 1000)
        }
        if (!isTaskScheduled.getAndSet(true)) {
            trackerTimerTask = object : TimerTask() {
                override fun run() {
                    if (isTrackerRunning.value) {
                        trackerTime.value++
                        if (trackerTime.value % 60 == 0) {
                            screenShotTakenTime.value++
                        }
                        println("Current minute: ${(trackerTime.value % 3600) / 60}")
                        println("Random times: ${randomTime.value}")
                        if (trackerIndex < randomTime.value.size && trackerTime.value > randomTime.value[trackerIndex]) {
                            takeScreenShot()
                            trackerIndex++
                            if (trackerIndex == randomTime.value.size) {
                                val overallStart = trackerTime.value
                                val overallEnd = overallStart + (screenshotLimit * 60)
                                trackerIndex = 0
                                setRandomTimes(
                                    randomTime,
                                    overallStart,
                                    overallEnd,
                                    screenShotFrequency
                                )
                            }
                        }
                    }
                }
            }
            timer.schedule(trackerTimerTask, 1000, 1000)
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
        viewModelScope.launch {
            screenShot.value = org.softsuave.bustlespot.screenshot.takeScreenShot()
            screenShotTakenTime.value = 0
        }

    }

    actual fun startScreenshotTask() {
        if (screenshotRepeatingTask == null) {
            screenshotRepeatingTask = object : TimerTask() {
                override fun run() {
                    if (!isPaused) {
                        val randomDelay = Random.nextLong(0, 60 * 1000)
                        screenshotOneShotTask?.cancel()
                        screenshotOneShotTask = object : TimerTask() {
                            override fun run() {
                                takeScreenShot()
                            }
                        }
                        timer.schedule(screenshotOneShotTask, randomDelay)
                    }
                }
            }
            timer.schedule(screenshotRepeatingTask, 0, 60 * 1000)
        }
    }

    actual fun pauseScreenshotTask() {
        isPaused = true
    }

    actual fun resumeScreenshotTask() {
        isPaused = false
    }

    actual fun stopScreenshotTask() {
        screenshotRepeatingTask?.cancel()
        screenshotRepeatingTask = null
        screenshotOneShotTask?.cancel()
        screenshotOneShotTask = null
    }

    actual fun updateTrackerTimer() {
        val newTime = trackerTime.value - customeTimeForIdleTime.value
        trackerTime.value = if (newTime < 0) 0 else newTime
    }

    actual fun startIdleTimer() {
    }

    actual fun addCustomTimeForIdleTime(time: Int) {
        customeTimeForIdleTime.value = time
    }

//    override fun onCleared() {
//        super.onCleared()
//        timer.cancel()
//    }

    actual fun setTrackerTime(trackerTime: Int, idealTime: Int) {
        this.trackerTime.value = trackerTime
        // this.idealTime.value = idealTime
    }

    actual fun setLastScreenShotTime(time: Int) {
        screenShotTakenTime.value = time
    }

    actual var startTime: Instant
        get() = TODO("Not yet implemented")
        set(value) {}
    actual var canCallApi: MutableStateFlow<Boolean>
        get() = TODO("Not yet implemented")
        set(value) {}

    actual fun getActivityData(): ActivityData {
        TODO("Not yet implemented")
    }

    actual fun getUntrackedActivityData(): ActivityData {
        TODO("Not yet implemented")
    }

}
