package org.softsuave.bustlespot

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.softsuave.bustlespot.App
import org.softsuave.bustlespot.di.initKoin
import java.awt.Dimension

fun main() = application {
    Runtime.getRuntime().addShutdownHook(Thread {
        // clean up logic
    })
    initKoin()
    Window(
        title = "Bustlespot",
        state = rememberWindowState(size = DpSize(width = 420.dp, height = 800.dp)),
        onCloseRequest = ::exitApplication,
        resizable = false
    ) {
        window.minimumSize = Dimension(350, 600)
        App {
            window.toFront()
            window.requestFocus()
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}