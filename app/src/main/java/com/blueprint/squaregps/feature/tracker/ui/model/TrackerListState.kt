package com.blueprint.squaregps.feature.tracker.ui.model

import androidx.compose.runtime.Immutable
import com.blueprint.squaregps.feature.tracker.domain.model.Tracker

@Immutable
data class TrackerListState (
    val list: List<Tracker> = emptyList(),
    val refreshing: Boolean = false,
    val errorMessage: String? = null,
) {
    val loading: Boolean get() = list.isEmpty()
}