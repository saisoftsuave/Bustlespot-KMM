import UIKit

@objc public class ScreenshotHelper: NSObject {
  @objc public static func takeScreenshot() -> UIImage? {
    guard let window = UIApplication.shared.windows.first(where: { $0.isKeyWindow }) else {
      return nil
    }
    UIGraphicsBeginImageContextWithOptions(window.bounds.size, false, UIScreen.main.scale)
    window.drawHierarchy(in: window.bounds, afterScreenUpdates: true)
    let image = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return image
  }
}
