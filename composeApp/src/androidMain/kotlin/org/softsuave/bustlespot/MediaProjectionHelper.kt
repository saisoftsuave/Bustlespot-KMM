package org.softsuave.bustlespot

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjection.Callback
import android.media.projection.MediaProjectionManager
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.compose.runtime.mutableStateOf
import java.io.File
import java.io.FileOutputStream

class MediaProjectionHelper(private val context: Context) {

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: android.hardware.display.VirtualDisplay? = null
    private var imageReader: ImageReader? = null
    private val handler = Handler(Looper.getMainLooper())  // Ensure UI updates work properly

    fun startScreenCapture(mediaProjectionResultCode: Int, resultData: Intent) {
        Log.d(TAG, "Starting screen capture...")

        val mediaProjectionManager =
            context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjection =
            mediaProjectionManager.getMediaProjection(mediaProjectionResultCode, resultData)

        if (mediaProjection == null) {
            Log.e(TAG, "MediaProjection is null. Cannot start screen capture.")
            return
        }

        Log.d(TAG, "MediaProjection initialized successfully.")

        // ✅ Register a callback before starting capture
        mediaProjection?.registerCallback(object : Callback() {
            override fun onStop() {
                super.onStop()
                Log.d(TAG, "MediaProjection stopped")
                stopScreenCapture()
            }
        }, handler)

        setupImageReader()
        createVirtualDisplay()
    }

    private fun setupImageReader() {
        Log.d(TAG, "Setting up ImageReader...")

        val metrics = getDisplayMetrics()
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        Log.d(TAG, "Screen dimensions: $width x $height")

        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2).apply {
            setOnImageAvailableListener({ reader ->
                Log.d(TAG, "New image available from ImageReader.")
                processImage(reader)
            }, handler)
        }
    }

    private fun createVirtualDisplay() {
        Log.d(TAG, "Creating VirtualDisplay...")

        val metrics = getDisplayMetrics()
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val density = metrics.densityDpi

        Log.d(TAG, "Screen dimensions: $width x $height, Density: $density")

        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "ScreenCapture",
            width, height, density,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader?.surface, null, handler
        )

        Log.d(TAG, "VirtualDisplay created successfully.")
    }

    private fun processImage(reader: ImageReader) {
        var image: Image? = null
        try {
            image = reader.acquireLatestImage()
            if (image != null) {
                Log.d(TAG, "Image acquired successfully.")
                val bitmap = imageToBitmap(image)
                saveBitmapToFile(bitmap)
                ScreenShot.screenShot.value = bitmap
            } else {
                Log.e(TAG, "Failed to acquire latest image.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing image", e)
        } finally {
            image?.close()
            Log.d(TAG, "Image closed.")
        }
    }

    private fun imageToBitmap(image: Image): Bitmap {
        Log.d(TAG, "Converting Image to Bitmap...")

        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * image.width

        Log.d(
            TAG,
            "Image details - Width: ${image.width}, Height: ${image.height}, PixelStride: $pixelStride, RowStride: $rowStride"
        )

        val bitmap = Bitmap.createBitmap(
            image.width + rowPadding / pixelStride,
            image.height, Bitmap.Config.ARGB_8888
        )
        bitmap.copyPixelsFromBuffer(buffer)

        Log.d(TAG, "Bitmap created successfully.")
        return bitmap
    }

    private fun saveBitmapToFile(bitmap: Bitmap) {
        Log.d(TAG, "Saving Bitmap to file...")

        val file = File(context.getExternalFilesDir(null), "screenshot.png")
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                Log.d(TAG, "Screenshot saved to ${file.absolutePath}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving screenshot", e)
        }
    }

    fun stopScreenCapture() {
        Log.d(TAG, "Stopping screen capture...")

        virtualDisplay?.release()
        virtualDisplay = null

        mediaProjection?.stop()
        mediaProjection = null

        imageReader?.close()
        imageReader = null

        Log.d(TAG, "Screen capture stopped.")
    }

    private fun getDisplayMetrics(): DisplayMetrics {
        val metrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

    object ScreenShot {
        var screenShot = mutableStateOf<Bitmap?>(null)
    }

    companion object {
        private const val TAG = "MediaProjectionHelper"
    }
}
