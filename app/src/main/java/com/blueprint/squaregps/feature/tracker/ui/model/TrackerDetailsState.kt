package com.blueprint.squaregps.feature.tracker.ui.model

import com.blueprint.squaregps.feature.tracker.domain.model.Tracker
import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState
import com.blueprint.squaregps.feature.tracker.domain.model.TrackerWithState

data class TrackerDetailsState (
    val tracker: Tracker? = null,
    val trackerState: TrackerState? = null,
    val loading: Boolean = false,
    val errorMessage: String? = null,
)