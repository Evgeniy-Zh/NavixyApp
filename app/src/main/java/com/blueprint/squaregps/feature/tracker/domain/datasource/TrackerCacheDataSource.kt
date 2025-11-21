package com.blueprint.squaregps.feature.tracker.domain.datasource

import com.blueprint.squaregps.feature.tracker.domain.model.Tracker
import kotlinx.coroutines.flow.Flow

interface TrackerCacheDataSource {
    fun getTrackerList(): Flow<List<Tracker>>
    fun getTracker(trackerId: Long): Flow<Tracker?>

    suspend fun updateTrackers(trackers: List<Tracker>)
    suspend fun updateTracker(tracker: Tracker)

    suspend fun clearTrackers()

}