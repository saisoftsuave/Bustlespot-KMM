package org.softsuave.bustlespot

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.softsuave.bustlespot.di.initKoin
import java.awt.Desktop
import java.awt.Dimension
import java.awt.Frame
import javax.swing.SwingUtilities

fun main() = application {
    Runtime.getRuntime().addShutdownHook(Thread {
        // clean up logic
    })

    NotifierManager.initialize(
        NotificationPlatformConfiguration.Desktop(
            showPushNotification = true
        )
    )
    initKoin()
    Window(
        title = "Bustlespot",
        state = rememberWindowState(size = DpSize(width = 420.dp, height = 800.dp)),
        onCloseRequest = ::exitApplication,
        resizable = false
    ) {
        window.minimumSize = Dimension(350, 600)
        App {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.APP_EVENT_FOREGROUND)) {
                desktop.requestForeground(true)
                window.toFront()
                window.requestFocus()
            } else {
                if (window.state == Frame.ICONIFIED) {
                    window.state = Frame.NORMAL
                }
                SwingUtilities.invokeLater {
                    window.toFront()
                    window.requestFocus()
                }
            }
        }
    }
}


@Preview
@Composable
fun AppPreview() {
    App()
}
