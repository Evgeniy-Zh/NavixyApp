package com.blueprint.squaregps.feature.tracker.domain.datasource

import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState

interface TrackerStateDataSource {
    suspend fun getTrackerStates(trackerIds: List<Long>): Map<Long, TrackerState>
    suspend fun getTrackerState(trackerId: Long): TrackerState?
}