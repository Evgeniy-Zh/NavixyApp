package com.blueprint.squaregps.feature.tracker.domain

import com.blueprint.squaregps.feature.tracker.domain.model.LatLng
import com.blueprint.squaregps.feature.tracker.domain.model.Location
import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState
import com.blueprint.squaregps.utils.testScope
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class TrackerPollerTest {

    lateinit var trackerPoller: TrackerPoller
    lateinit var trackerRepository: TrackerStateRepository

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

    val requestIds = trackerStates.keys.toList()

    @Before
    fun setUp() {
        trackerRepository = mockk<TrackerStateRepository>()

        trackerPoller = TrackerPoller(
            trackerStateRepository = trackerRepository
        )
    }

    @Test
    fun startPolling() = runTest {

        val flow = MutableSharedFlow<Map<Long, TrackerState>>()

        coEvery { trackerRepository.fetchTrackerStates(requestIds) } answers {
            launch {
                flow.emit(trackerStates)
            }
        }

        every { trackerRepository.observeTrackerStates(requestIds) } answers { flow }

        advanceUntilIdle()
        testScope {
            launch {
                trackerPoller.observeErrors().filterNotNull().take(1).collect { exception ->
                    throw exception
                }
            }

            launch {
                trackerPoller.startPolling(requestIds)
                    .timeout(2.seconds)
                    .take(3)
                    .collect { result ->
                        println(result)
                        assertEquals(requestIds, result.keys.toList())
                    }
                this@testScope.cancel()
            }
        }

    }

    @Test
    fun observeErrors() = runTest {
        val flow = MutableSharedFlow<Map<Long, TrackerState>>()

        coEvery { trackerRepository.fetchTrackerStates(requestIds) } throws Exception("Failed to fetch tracker states")

        every { trackerRepository.observeTrackerStates(requestIds) } answers { flow }


        advanceUntilIdle()
        testScope {
            launch {
                trackerPoller.startPolling(requestIds).collect { }
            }

            launch {
                trackerPoller.observeErrors()
                    .filterNotNull()
                    .timeout(2.seconds)
                    .take(1)
                    .collect { exception ->
                        assertEquals("Failed to fetch tracker states", exception.message)
                        this@testScope.cancel()
                    }
            }
        }
    }

}
