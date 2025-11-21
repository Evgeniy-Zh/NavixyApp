package com.blueprint.squaregps.feature.tracker.data.impl

import com.blueprint.squaregps.feature.tracker.data.api.TrackerApi
import com.blueprint.squaregps.feature.tracker.data.dto.StateDto
import com.blueprint.squaregps.feature.tracker.data.dto.TrackerDto
import com.blueprint.squaregps.feature.tracker.data.dto.TrackerStateRequest
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerDataSource
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerStateDataSource
import com.blueprint.squaregps.feature.tracker.domain.model.LatLng
import com.blueprint.squaregps.feature.tracker.domain.model.Location
import com.blueprint.squaregps.feature.tracker.domain.model.Tracker
import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState

class TrackerDataSourceImpl(
    private val trackerApi: TrackerApi
) : TrackerDataSource, TrackerStateDataSource {
    override suspend fun getTrackerList(): List<Tracker> {
        return trackerApi.getTrackerList().list.toDomain()
    }

    override suspend fun getTracker(trackerId: Long): Tracker {
        return trackerApi.getTracker(trackerId).tracker.toDomain()
    }

    override suspend fun getTrackerStates(trackerIds: List<Long>): Map<Long, TrackerState> {
        val request = TrackerStateRequest(trackerIds = trackerIds)
        return trackerApi.getTrackerStates(request).states.toDomain()
    }

    override suspend fun getTrackerState(trackerId: Long): TrackerState? {
        //TODO: use get_state endpoint to get state of single tracker.
        val request = TrackerStateRequest(trackerIds = listOf(trackerId))
        return trackerApi.getTrackerStates(request).states?.get(trackerId)?.toDomain()
    }
}

fun StateDto.toDomain(): TrackerState = TrackerState(
    sourceId = sourceId,
    location = Location(
        latLng = gps?.location?.let { LatLng(it.lat, it.lng) },
        speed = gps?.speed,
        heading = gps?.heading,
    )
)

fun Map<Long, StateDto>?.toDomain(): Map<Long, TrackerState> =
    this?.mapValues { (id, dto) ->
        dto.toDomain()
    } ?: emptyMap()

fun TrackerDto.toDomain(): Tracker = Tracker(
    id = id,
    label = label,
    model = source?.model,
    deviceId = source?.deviceId,
)

fun List<TrackerDto>?.toDomain(): List<Tracker> = this?.map { dto: TrackerDto ->
    dto.toDomain()
} ?: emptyList()