package org.softsuave.bustlespot

import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import org.softsuave.bustlespot.screenshot.ComponentActivityReference
import org.softsuave.bustlespot.ui.MediaProjectionService

class MainActivity : ComponentActivity() {
    private lateinit var mediaProjectionHelper: MediaProjectionHelper
    private val REQUEST_CODE_SCREEN_CAPTURE = 100
    private val screenCaptureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.d("MainActivity Screen capture permission granted. Starting service...")

            // Start the foreground service with the result data
            val serviceIntent = Intent(this, MediaProjectionService::class.java).apply {
                putExtra("resultCode", result.resultCode) // Pass resultCode
                putExtra("data", result.data) // Pass data
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent) // Use startForegroundService for Android O+
            } else {
                startService(serviceIntent) // Use startService for older versions
            }
        } else {
            println("Screen capture permission denied")
        }
    }
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //requestScreenCapturePermission()
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            App()
        }
        mediaProjectionHelper = MediaProjectionHelper(this)
        ComponentActivityReference.setActivity(this)
        checkAndRequestNotificationPermission()
    }

    private fun requestScreenCapturePermission() {
        val mediaProjectionManager =
            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val captureIntent = mediaProjectionManager.createScreenCaptureIntent()

        screenCaptureLauncher.launch(captureIntent)
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
            else{
                //requestScreenCapturePermission()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ComponentActivityReference.clear()
        mediaProjectionHelper.stopScreenCapture()
    }


}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}