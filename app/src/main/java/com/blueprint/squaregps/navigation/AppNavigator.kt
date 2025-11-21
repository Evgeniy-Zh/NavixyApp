package com.blueprint.squaregps.navigation

interface AppNavigator {
    fun back()
    fun navigateTo(route: Route, popUpTo: Route? = null)
}