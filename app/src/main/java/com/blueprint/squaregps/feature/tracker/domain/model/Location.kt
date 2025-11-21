package com.blueprint.squaregps.feature.tracker.domain.model

data class Location(
    val latLng: LatLng?,
    val heading: Int?,
    val speed: Int?,
)

data class LatLng(
    val lat: Double,
    val lng: Double
)