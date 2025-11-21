package com.blueprint.squaregps.feature.tracker.domain

import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerCacheDataSource
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerDataSource
import com.blueprint.squaregps.feature.tracker.domain.model.Tracker
import kotlinx.coroutines.flow.Flow

class TrackerRepository(
    private val trackerDataSource: TrackerDataSource,
    private val trackerCacheDataSource: TrackerCacheDataSource,
) {

    fun observeTrackers(): Flow<List<Tracker>> = trackerCacheDataSource.getTrackerList()

    suspend fun fetchTrackers() {
        val trackers = trackerDataSource.getTrackerList()
        trackerCacheDataSource.updateTrackers(trackers)
    }

    fun observeTracker(trackerId: Long): Flow<Tracker?> =
        trackerCacheDataSource.getTracker(trackerId)

    suspend fun fetchTracker(trackerId: Long) {
        val tracker = trackerDataSource.getTracker(trackerId)
        trackerCacheDataSource.updateTracker(tracker)
    }
}


