package org.softsuave.bustlespot


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.softsuave.bustlespot.mainnavigation.RootNavigationGraph
import org.softsuave.bustlespot.theme.AppTheme


@Composable
internal fun App(onFocusReceived: () -> Unit = {}) {
    val navController = rememberNavController()
    AppTheme {
        RootNavigationGraph(navController, onFocusReceived)
    }
}

