package org.softsuave.bustlespot.accessability

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import org.koin.java.KoinJavaComponent.inject

class AccessibilityPermission {
    private val activity: Context by inject(
        Context::class.java
    )

    private fun isAccessibilityServiceEnabledAlternative(
        context: Context,
        service: Class<out AccessibilityService>
    ): Boolean {
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(
            AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        )
        return enabledServices.any { it.resolveInfo.serviceInfo.name == service.name }
    }

    fun requestAccessibilityPermission() {
        val service: Class<out AccessibilityService> = TrackerAccessibilityService::class.java
        if (isAccessibilityServiceEnabledAlternative(activity, service)) {
            Toast.makeText(activity, "Accessibility permission already granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            val accessibilityIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            accessibilityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(accessibilityIntent)
        }
    }
}