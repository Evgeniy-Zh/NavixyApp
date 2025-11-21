package com.blueprint.squaregps.feature.tracker.domain.datasource

import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState
import kotlinx.coroutines.flow.Flow

interface TrackerStateCacheDataSource {

    fun getTrackerStates(trackerIds: List<Long>): Flow<Map<Long, TrackerState?>>
    fun getTrackerState(trackerId: Long): Flow<TrackerState?>

    suspend fun updateTrackerStates(trackerStates: Map<Long, TrackerState>)
    suspend fun updateTrackerState(trackerId: Long, trackerState: TrackerState)

    suspend fun clearTrackerStates()
}