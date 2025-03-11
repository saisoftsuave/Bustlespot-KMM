package org.softsuave.bustlespot.utils

import org.koin.java.KoinJavaComponent.inject
import org.softsuave.bustlespot.accessability.AccessibilityPermission
import org.softsuave.bustlespot.screenshot.ComponentActivityReference
import org.softsuave.bustlespot.screenshot.MediaProjection

// Define request codes for your permissions.
const val MEDIA_PROJECTION_REQUEST_CODE = 1001
private const val NOTIFICATION_REQUEST_CODE = 1002

actual fun requestPermission(
    onPermissionGranted: () -> Unit,
) {
    val accessibilityPermission: AccessibilityPermission by inject(AccessibilityPermission::class.java)
//    val mediaProjection = MediaProjection(
//        ComponentActivityReference.getActivity()
//    )
    accessibilityPermission.requestAccessibilityPermission(
        onPermissionGranted = onPermissionGranted
    )
   // mediaProjection.requestScreenCapturePermission()
}