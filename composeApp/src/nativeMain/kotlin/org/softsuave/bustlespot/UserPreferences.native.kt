package org.softsuave.bustlespot

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import platform.Foundation.NSUserDefaults

actual fun createSettings(): ObservableSettings {
    val delegate: NSUserDefaults = NSUserDefaults.standardUserDefaults
    return NSUserDefaultsSettings(delegate)
}