package com.blueprint.squaregps.navigation

import kotlinx.serialization.Serializable

interface Route

@Serializable
object TrackerListRoute: Route

@Serializable
data class TrackerDetailsRoute(val trackerId: Long): Route

@Serializable
object LoginRoute: Route