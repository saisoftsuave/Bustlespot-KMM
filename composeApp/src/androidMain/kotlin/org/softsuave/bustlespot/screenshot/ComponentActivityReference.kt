package org.softsuave.bustlespot.screenshot
import java.lang.ref.WeakReference
import androidx.activity.ComponentActivity

object ComponentActivityReference {
    private var activityRef: WeakReference<ComponentActivity>? = null

    fun setActivity(activity: ComponentActivity) {
        activityRef = WeakReference(activity)
    }

    fun getActivity(): ComponentActivity? {
        return activityRef?.get()
    }

    fun clear() {
        activityRef?.clear()
        activityRef = null
    }
}
