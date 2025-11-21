package com.blueprint.squaregps.feature.tracker.domain

import com.blueprint.squaregps.core.exception.suspendCatching
import com.blueprint.squaregps.feature.tracker.domain.model.TrackerState
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TrackerPoller(
    private val trackerStateRepository: TrackerStateRepository,
) {

    private val errors = MutableStateFlow<Exception?>(null)

    fun startPolling(ids: List<Long>, interval: Long = 1_000): Flow<Map<Long, TrackerState?>> = channelFlow {

        launchPolling(ids, interval)

        trackerStateRepository
            .observeTrackerStates(ids)
            .collect { states ->
                send(states)
            }

    }

    fun observeErrors(): Flow<Exception?> = errors

    private fun ProducerScope<*>.launchPolling(
        ids: List<Long>,
        interval: Long,
    ) = launch {
        while (isActive) {
            suspendCatching(
                run = {
                    trackerStateRepository.fetchTrackerStates(ids)
                    errors.value = null
                },
                onException = { e ->
                    errors.value = e
                }
            )

            delay(interval)
        }
    }

}