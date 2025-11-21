package com.blueprint.squaregps.navigation

sealed class NavAction {
    class NavigateTo(val route: Route, val popupTo: Route? = null) : NavAction()
    class NavigateBack(val route: Route? = null) : NavAction()
}