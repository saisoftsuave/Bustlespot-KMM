package org.softsuave.bustlespot.notifications

import com.mmk.kmpnotifier.notification.NotificationImage
import com.mmk.kmpnotifier.notification.NotifierManager
import kotlin.random.Random

actual fun sendLocalNotification(
    notificationTitle: String,
    notificationBody: String,
    imageFile: String
) {
    val notifier = NotifierManager.getLocalNotifier()
    notifier.notify {
        id = Random.nextInt(0, Int.MAX_VALUE)
        title = notificationTitle
        body = notificationBody
        image = NotificationImage.File(imageFile)
    }
}