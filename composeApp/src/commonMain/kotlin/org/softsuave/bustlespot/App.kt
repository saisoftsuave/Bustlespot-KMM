package org.softsuave.bustlespot


import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.softsuave.bustlespot.mainnavigation.RootNavigationGraph


@Composable
internal fun App(onFocusReceived: () -> Unit = {}) {
    val navController = rememberNavController()
    RootNavigationGraph(navController, onFocusReceived)
}

