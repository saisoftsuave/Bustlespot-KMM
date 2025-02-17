package org.softsuave.bustlespot.mainnavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.koin.compose.koinInject
import org.softsuave.bustlespot.SessionManager
import org.softsuave.bustlespot.auth.navigation.authNavGraph
import org.softsuave.bustlespot.auth.navigation.homeNavGraph


@Composable
fun RootNavigationGraph(navController: NavHostController, onFocusReceived: () -> Unit = {}) {
    val sessionManager: SessionManager = koinInject()
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState()


    LaunchedEffect(sessionManager.isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(
                route = Graph.AUTHENTICATION
            )
        }
    }

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = if (isLoggedIn) Graph.HOME else Graph.AUTHENTICATION
    ) {
        authNavGraph(navController)
        homeNavGraph(navController, onFocusReceived)
    }
}


object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val ORGANISATION = "organisation_graph"
}
