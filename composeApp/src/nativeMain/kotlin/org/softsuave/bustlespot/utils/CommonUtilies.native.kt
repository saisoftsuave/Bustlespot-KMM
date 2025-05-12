//package org.softsuave.bustlespot.utils
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.ImageBitmap
//import kotlinx.cinterop.ExperimentalForeignApi
//import platform.Foundation.NSData
//import platform.Foundation.getBytes
//import platform.UIKit.UIGraphicsBeginImageContextWithOptions
//import platform.UIKit.UIGraphicsEndImageContext
//import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
//import platform.UIKit.UIImage
//import platform.UIKit.UIScreen
//import platform.UIKit.UIView
//import platform.CoreGraphics.CGImageRef
//import platform.CoreGraphics.CGDataProviderRef
//import org.jetbrains.skia.Image as Image
//
//actual fun isAndroid(): Boolean = false
//
//@Composable
//actual fun handleBackPress(onBack: () -> Unit) {
//}
//
//// Function to take a screenshot of a given UIView
//@OptIn(ExperimentalForeignApi::class)
//fun takeScreenshotOfView(view: UIView): UIImage {
//    UIGraphicsBeginImageContextWithOptions(view.bounds., false, UIScreen.mainScreen.scale)
//    view.drawViewHierarchyInRect(view.bounds, afterScreenUpdates = true)
//    val image = UIGraphicsGetImageFromCurrentImageContext()
//    UIGraphicsEndImageContext()
//    return image
//}
//@OptIn(ExperimentalForeignApi::class)
//fun UIImage.toImageBitmap(): ImageBitmap? {
//    // Access the cgImage property, which may return CGImageRef?
//    val cgImage: CGImageRef? = (this as? UIImage)?.CGImage
//    if (cgImage == null) return null // Fallback for null image
//
//    // Get the data provider from the CGImage
//    val dataProvider: CGDataProviderRef? = cgImage.dataProvider()
//    if (dataProvider == null) null // Fallback for null provider
//
//    // Extract the raw data
//    val data: NSData? = dataProvider.data as? NSData
//    if (data == null) return ImageBitmap.ImageBitmapImpl(0, 0) // Fallback for null data
//
//    // Convert NSData to ByteArray
//    val bytes = ByteArray(data.length.toInt())
//    data.getBytes(bytes, 0, bytes.size)
//
//    // Create a Skia image and convert to Compose ImageBitmap
//    val skiaImage = Image.makeFromEncoded(bytes)
//    return skiaImage.asComposeImageBitmap()
//}
//
//// Placeholder implementation (adjust based on your actual ImageBitmap class)
//fun Image.asComposeImageBitmap(): ImageBitmap {
//    // Implementation depends on your setup; this is a stub
//    return ImageBitmap.ImageBitmapImpl(this.width, this.height)
//}
//
//// Stub for ImageBitmap implementation
//object ImageBitmap {
//    class ImageBitmapImpl(val width: Int, val height: Int) : ImageBitmap
//}