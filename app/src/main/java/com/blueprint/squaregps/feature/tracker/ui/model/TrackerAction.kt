package com.blueprint.squaregps.feature.tracker.ui.model

import com.blueprint.squaregps.feature.tracker.domain.model.Tracker

sealed interface TrackerAction {
    class OpenTrackerDetails(val tracker: Tracker) : TrackerAction
    object RefreshTrackerList : TrackerAction
}