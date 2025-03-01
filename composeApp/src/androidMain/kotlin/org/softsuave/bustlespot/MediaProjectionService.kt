package org.softsuave.bustlespot.ui

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.softsuave.bustlespot.MediaProjectionHelper
import org.softsuave.bustlespot.R

class MediaProjectionService : Service() {

    private var mediaProjectionHelper: MediaProjectionHelper? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created.")
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started.")

        val resultCode = intent?.getIntExtra("resultCode", Activity.RESULT_CANCELED) ?: Activity.RESULT_CANCELED
        val resultData = intent?.getParcelableExtra<Intent>("data")

        Log.d(TAG, "Received resultCode: $resultCode")
        Log.d(TAG, "Received resultData: $resultData")

        if (resultCode == RESULT_OK && resultData != null) {
            if (mediaProjectionHelper == null) {
                mediaProjectionHelper = MediaProjectionHelper(this)
            }

            mediaProjectionHelper?.startScreenCapture(resultCode, resultData)
        } else {
            Log.e(TAG, "Invalid resultCode or missing data. Stopping service.")
            stopSelf() // Stop service if permission is denied
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed.")
        mediaProjectionHelper?.stopScreenCapture()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startForegroundService() {
        val channelId = "media_projection_service_channel"
        val channelName = "Media Projection Service"
        val notificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Screen Capture")
            .setContentText("Capturing screen...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION)
        } else {
            startForeground(1, notification)
        }
    }

    companion object {
        private const val TAG = "MediaProjectionService"
    }
}
