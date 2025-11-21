package com.blueprint.squaregps.feature.tracker.domain

import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerStateCacheDataSource
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerStateDataSource
import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
class TrackerStateRepository(
    private val trackerStateDataSource: TrackerStateDataSource,
    private val trackerStateCacheDataSource: TrackerStateCacheDataSource,
) {

    @Suppress("UNCHECKED_CAST")
    fun observeTrackerStates(trackerIds: List<Long>): Flow<Map<Long, TrackerState>> {
        return trackerStateCacheDataSource.getTrackerStates(trackerIds)
            .map { states -> states.filterValues { it != null } as Map<Long, TrackerState> }
    }

    suspend fun fetchTrackerStates(trackerIds: List<Long>) {
        val states = trackerStateDataSource.getTrackerStates(trackerIds)
        trackerStateCacheDataSource.updateTrackerStates(states)
    }

    fun observeTrackerState(trackerId: Long): Flow<TrackerState?> {
        return trackerStateCacheDataSource.getTrackerState(trackerId)
    }

    suspend fun fetchTrackerState(trackerId: Long) {
        val tracker = trackerStateDataSource.getTrackerState(trackerId)
        if (tracker != null) {
            trackerStateCacheDataSource.updateTrackerState(trackerId, tracker)
        }
    }


}