package org.softsuave.bustlespot.utils



actual fun requestPermission(onPermissionGranted: () -> Unit) {
    onPermissionGranted()
}