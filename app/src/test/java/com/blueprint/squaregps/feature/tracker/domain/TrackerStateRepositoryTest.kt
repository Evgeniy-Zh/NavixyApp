package com.blueprint.squaregps.feature.tracker.domain

import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerStateCacheDataSource
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerStateDataSource
import com.blueprint.squaregps.feature.tracker.domain.model.LatLng
import com.blueprint.squaregps.feature.tracker.domain.model.Location
import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created on 10.02.2026.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TrackerStateRepositoryTest {

    lateinit var trackerStateRepository: TrackerStateRepository
    lateinit var trackerStateDataSource: TrackerStateDataSource
    lateinit var trackerStateCacheDataSource: TrackerStateCacheDataSource

    val trackerStates = mapOf(
        1L to TrackerState(
            sourceId = 11,
            location = Location(latLng = LatLng(32.323, 43.432), heading = 2, speed = 12)
        ),
        2L to TrackerState(
            sourceId = 31,
            location = Location(latLng = LatLng(32.432, 43.665), heading = 3, speed = 5)
        ),

        )

    @Before
    fun setUp() {

        trackerStateDataSource = object : TrackerStateDataSource {
            override suspend fun getTrackerStates(trackerIds: List<Long>): Map<Long, TrackerState> {
                return trackerStates.filterKeys { trackerIds.contains(it) }
            }

            override suspend fun getTrackerState(trackerId: Long): TrackerState? {
                return trackerStates[trackerId]
            }
        }

        trackerStateCacheDataSource = object : TrackerStateCacheDataSource {
            val data = MutableStateFlow(mapOf<Long, TrackerState?>())
            override fun getTrackerStates(trackerIds: List<Long>): Flow<Map<Long, TrackerState?>> {
                return data
            }

            override fun getTrackerState(trackerId: Long): Flow<TrackerState?> {
                return data.map { it[trackerId] }
            }

            override suspend fun updateTrackerStates(trackerStates: Map<Long, TrackerState>) {
                data.update { value ->
                    value + trackerStates
                }
            }

            override suspend fun updateTrackerState(
                trackerId: Long,
                trackerState: TrackerState
            ) {
                data.update { value ->
                    value + Pair(trackerId, trackerState)
                }
            }

            override suspend fun clearTrackerStates() {
                data.update { emptyMap() }
            }
        }

        trackerStateRepository =
            TrackerStateRepository(trackerStateDataSource, trackerStateCacheDataSource)
    }

    @Test
    fun observeTrackerStates() = runTest {
        advanceUntilIdle()
        val ids = trackerStates.keys.toList()

        trackerStateRepository.observeTrackerStates(ids)
            .take(1)
            .collect {
                assert(it.isEmpty())
            }

        trackerStateRepository.fetchTrackerStates(ids)


        trackerStateRepository.observeTrackerStates(ids)
            .take(1)
            .collect {
                assertEquals(trackerStates, it)
            }

        trackerStateRepository.observeTrackerStates(listOf(100L))
            .take(1)
            .collect {
                assertEquals(trackerStates, it)
            }
    }

}