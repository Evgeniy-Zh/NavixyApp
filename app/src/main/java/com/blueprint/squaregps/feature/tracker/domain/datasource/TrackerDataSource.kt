package com.blueprint.squaregps.feature.tracker.domain.datasource

import com.blueprint.squaregps.feature.tracker.domain.model.Tracker
import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState


interface TrackerDataSource {
    suspend fun getTrackerList(): List<Tracker>
    suspend fun getTracker(trackerId: Long): Tracker
}