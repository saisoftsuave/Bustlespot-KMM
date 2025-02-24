package org.softsuave.bustlespot.browser

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openWebLink(url: String) {
    val nsUrl = NSURL(string = url)
    UIApplication.sharedApplication.openURL(nsUrl)
}