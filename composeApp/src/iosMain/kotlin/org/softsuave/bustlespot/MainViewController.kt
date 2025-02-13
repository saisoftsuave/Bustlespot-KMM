package org.softsuave.bustlespot

import androidx.compose.ui.window.ComposeUIViewController
import org.softsuave.bustlespot.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}