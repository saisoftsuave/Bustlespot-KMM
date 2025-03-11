package org.softsuave.bustlespot.notifications


expect fun sendLocalNotification(
    notificationTitle: String, notificationBody: String, imageFile: String?
)