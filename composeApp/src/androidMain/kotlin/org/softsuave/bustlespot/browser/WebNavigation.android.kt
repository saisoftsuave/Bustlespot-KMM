package org.softsuave.bustlespot.browser

import android.content.Intent
import android.net.Uri

actual fun openWebLink(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}