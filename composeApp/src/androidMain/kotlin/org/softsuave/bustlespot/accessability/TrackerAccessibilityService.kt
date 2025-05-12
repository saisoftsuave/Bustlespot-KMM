package org.softsuave.bustlespot.accessability

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.softsuave.bustlespot.Log
import org.softsuave.bustlespot.accessability.GlobalAccessibilityEvents.lastClickTime
import kotlin.time.Duration


class TrackerAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            when (it.eventType) {
                AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                    if(GlobalAccessibilityEvents.isListening.value){
                        GlobalAccessibilityEvents.mouseCountFlow.value++
                        lastClickTime = Clock.System.now()
                    }
                    Log.d("TrackerService Mouse Clicked: ${GlobalAccessibilityEvents.mouseCountFlow.value}")
                }
                AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                    if(GlobalAccessibilityEvents.isListening.value){
                        GlobalAccessibilityEvents.mouseMotionCountFlow.value++
                        lastClickTime = Clock.System.now()
                    }
                    Log.d("TrackerService Mouse Scrolled: ${GlobalAccessibilityEvents.mouseMotionCountFlow.value}")
                }
                else ->{
                    if(GlobalAccessibilityEvents.isListening.value){
                        GlobalAccessibilityEvents.mouseCountFlow.value++
                        lastClickTime = Clock.System.now()
                    }
                    Log.d("TrackerService Mouse Clicked: ${GlobalAccessibilityEvents.mouseCountFlow.value}")
                }
            }
        }
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            GlobalAccessibilityEvents.keyCountFlow.value++
            Log.d("TrackerService Key Pressed: ${GlobalAccessibilityEvents.keyCountFlow.value}")
        }
        return super.onKeyEvent(event)
    }

    override fun onInterrupt() {
        Log.d("TrackerService Interrupt")
    }

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()
        info.apply {
            eventTypes =
                AccessibilityEvent.TYPE_VIEW_CLICKED or AccessibilityEvent.TYPE_VIEW_FOCUSED or AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION or AccessibilityEvent.TYPE_VIEW_SCROLLED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
            notificationTimeout = 100
        }
        Log.d("TrackerService Connected")
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        this.serviceInfo = info
    }
}


object GlobalAccessibilityEvents {
    val keyCountFlow = MutableStateFlow(0)
    val mouseCountFlow = MutableStateFlow(0)
    val mouseMotionCountFlow = MutableStateFlow(0)
    var lastClickTime : Instant = Instant.DISTANT_PAST
    var isListening = MutableStateFlow(false)

    fun resetClickCounts(){
        keyCountFlow.value=0
        mouseMotionCountFlow.value=0
        mouseCountFlow.value=0
    }
}