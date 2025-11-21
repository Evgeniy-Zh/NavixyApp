package com.blueprint.squaregps.feature.tracker.ui.model

import com.blueprint.squaregps.feature.tracker.domain.model.Tracker


data class TrackerListState (
    val list: List<Tracker> = emptyList(),
    val refreshing: Boolean = false,
    val errorMessage: String? = null,
) {
    val loading: Boolean get() = list.isEmpty()
}