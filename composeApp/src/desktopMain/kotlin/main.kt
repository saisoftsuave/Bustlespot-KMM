package org.softsuave.bustlespot

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowDecoration
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import bustlespot.composeapp.generated.resources.Res
import bustlespot.composeapp.generated.resources.loginBustleIcon
import bustlespot.composeapp.generated.resources.logoWhite
import com.github.kwhat.jnativehook.GlobalScreen
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.jetbrains.compose.resources.painterResource
import org.softsuave.bustlespot.App
import org.softsuave.bustlespot.di.initKoin
import java.awt.Desktop
import java.awt.Frame
import javax.swing.SwingUtilities

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    // Cleanup when the app closes
    Runtime.getRuntime().addShutdownHook(Thread {
        try {
            GlobalScreen.unregisterNativeHook()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // clean up logic
    })

    // Initialize Notifier for push notifications
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Desktop(
            showPushNotification = true
        )
    )

    // Initialize Koin for dependency injection
    initKoin()


    Window(
        title = "Bustlespot",
        decoration = WindowDecoration.SystemDefault,
        state = rememberWindowState(size = DpSize(width = 420.dp, height = 800.dp)),
        onCloseRequest = ::exitApplication,
        resizable = false,
        icon = painterResource(Res.drawable.loginBustleIcon),
    ) {
        App(
            onFocusReceived = {
                val desktop = Desktop.getDesktop()
                if (desktop.isSupported(Desktop.Action.APP_EVENT_FOREGROUND)) {
                    desktop.requestForeground(true)
                    window.toFront()
                    window.requestFocus()
                } else {
                    if (window.state == Frame.ICONIFIED) {
                        window.extendedState = Frame.NORMAL
                    }
                    SwingUtilities.invokeLater {
                        window.isAlwaysOnTop = true
                        window.toFront()
                        window.requestFocus()
                        window.isAlwaysOnTop = false
                    }
                }
            },
            onMinimizeClick = {
                window.let {
                    it.isMinimized = false
                    SwingUtilities.invokeLater { it.isMinimized = true }
                }
            },
            onCloseClick = {
                exitApplication()
            }
        )
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}
