package org.softsuave.bustlespot.organisation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import org.softsuave.bustlespot.mainnavigation.Graph

sealed class OrganisationScreen(val route:String){
    data object SelectionScreen: OrganisationScreen("selection_screen")
}

fun NavGraphBuilder.orgNavGraph(){
    navigation(
        startDestination = OrganisationScreen.SelectionScreen.route,
        route = Graph.ORGANISATION
    ){

    }
}
