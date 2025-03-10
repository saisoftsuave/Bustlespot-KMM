package org.softsuave.bustlespot.screenshot

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.io.File
import javax.imageio.ImageIO

fun captureFullScreenScreenshot(imageName: String): ImageBitmap? {
    return try {
        // Get the entire screen dimensions
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val screenRect = Rectangle(screenSize)

        // Create Robot instance
        val robot = Robot()

        // Capture the entire screen
        val screenshot = robot.createScreenCapture(screenRect)

        screenshot.toComposeImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


actual fun takeScreenShot(): ImageBitmap? {
    return captureFullScreenScreenshot(
        "screen"
    )
}