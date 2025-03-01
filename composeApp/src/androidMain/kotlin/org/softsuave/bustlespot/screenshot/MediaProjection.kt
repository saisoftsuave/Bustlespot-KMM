package org.softsuave.bustlespot.screenshot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.softsuave.bustlespot.Log
import org.softsuave.bustlespot.ui.MediaProjectionService

class MediaProjection(private val activity: ComponentActivity?) {

    private val screenCaptureLauncher: ActivityResultLauncher<Intent>? = activity?.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("Screen capture permission granted. Starting service...")

            startMediaProjectionService(activity, result.resultCode, result.data)
        } else {
            Log.e("Screen capture permission denied")
        }
    }

    fun requestScreenCapturePermission() {
        val mediaProjectionManager =
            activity?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager
        val captureIntent = mediaProjectionManager?.createScreenCaptureIntent()

        captureIntent?.let { screenCaptureLauncher?.launch(it) }
    }

    private fun startMediaProjectionService(activity: Activity, resultCode: Int, data: Intent?) {
        val serviceIntent = Intent(activity, MediaProjectionService::class.java).apply {
            putExtra("resultCode", resultCode)
            putExtra("data", data)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(serviceIntent)
        } else {
            activity.startService(serviceIntent)
        }
    }
}
