package com.blueprint.squaregps.feature.tracker.data.impl

import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerCacheDataSource
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerStateCacheDataSource
import com.blueprint.squaregps.feature.tracker.domain.model.Tracker
import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TrackerCacheDataSourceImpl : TrackerCacheDataSource, TrackerStateCacheDataSource {

    private val trackers = MutableStateFlow<Map<Long, Tracker>>(emptyMap())
    private val trackerStates = MutableStateFlow<Map<Long, TrackerState>>(emptyMap())

    override fun getTrackerList(): Flow<List<Tracker>> {
        return trackers.map { it.values.toList() }
    }

    override fun getTracker(trackerId: Long): Flow<Tracker?> {
        return trackers.map { it[trackerId] }
    }

    override suspend fun updateTrackers(trackers: List<Tracker>) {
        this.trackers.update {
            trackers.associateBy { it.id }
        }
    }

    override suspend fun updateTracker(tracker: Tracker) {
        trackers.update {
            it + Pair(tracker.id, tracker)
        }
    }

    override fun getTrackerStates(trackerIds: List<Long>): Flow<Map<Long, TrackerState?>> {
        return trackerStates.map { states -> states.filterKeys { trackerIds.contains(it) } }
    }

    override fun getTrackerState(trackerId: Long): Flow<TrackerState?> {
        return trackerStates.map { it[trackerId] }
    }

    override suspend fun updateTrackerStates(trackerStates: Map<Long, TrackerState>) {
        this.trackerStates.update { trackerStates }
    }

    override suspend fun updateTrackerState(
        trackerId: Long,
        trackerState: TrackerState
    ) {
        trackerStates.update {
            it + Pair(trackerId, trackerState)
        }
    }

    override suspend fun clearTrackers() {
        trackers.update { emptyMap() }
    }

    override suspend fun clearTrackerStates() {
        trackers.update { emptyMap() }
        trackerStates.update { emptyMap() }
    }

}