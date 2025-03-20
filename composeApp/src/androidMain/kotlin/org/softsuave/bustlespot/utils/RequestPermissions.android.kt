package org.softsuave.bustlespot.utils

import org.koin.java.KoinJavaComponent.inject
import org.softsuave.bustlespot.accessability.AccessibilityPermission
import org.softsuave.bustlespot.screenshot.ComponentActivityReference

actual fun requestPermission(
    onPermissionGranted: () -> Unit,
) {
    val accessibilityPermission: AccessibilityPermission by inject(AccessibilityPermission::class.java)
//    val mediaProjection = MediaProjection(
//    ComponentActivityReference.getActivity()
    ComponentActivityReference.getActivity()
    accessibilityPermission.requestAccessibilityPermission(
        onPermissionGranted = {
            onPermissionGranted()
        }
    )
}