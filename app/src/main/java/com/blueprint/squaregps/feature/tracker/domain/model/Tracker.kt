package com.blueprint.squaregps.feature.tracker.domain.model

data class Tracker(
    val id: Long,
    val label: String,
    val model: String?,
    val deviceId: Long?,
)