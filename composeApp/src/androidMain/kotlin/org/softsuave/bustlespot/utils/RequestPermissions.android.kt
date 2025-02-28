package org.softsuave.bustlespot.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import org.koin.java.KoinJavaComponent.inject
import org.softsuave.bustlespot.accessability.AccessibilityPermission

// Define request codes for your permissions.
const val MEDIA_PROJECTION_REQUEST_CODE = 1001
private const val NOTIFICATION_REQUEST_CODE = 1002

actual fun requestPermission() {
    val accessibilityPermission: AccessibilityPermission by inject(AccessibilityPermission::class.java)
    accessibilityPermission.requestAccessibilityPermission()
}


fun notificationPermission(
    activity: Activity
) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
        NOTIFICATION_REQUEST_CODE
    )
}