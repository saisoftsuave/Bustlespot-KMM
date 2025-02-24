package org.softsuave.bustlespot.browser

import java.awt.Desktop
import java.net.URI

actual fun openWebLink(url: String) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(url))
    }
}
