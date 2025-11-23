package com.blueprint.squaregps.feature.tracker.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueprint.squaregps.core.exception.suspendCatching
import com.blueprint.squaregps.core.ui.vmSharingStarted
import com.blueprint.squaregps.feature.tracker.domain.TrackerPoller
import com.blueprint.squaregps.feature.tracker.domain.TrackerRepository
import com.blueprint.squaregps.feature.tracker.ui.model.TrackerDetailsState
import com.blueprint.squaregps.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackerDetailsViewModel(
    private val trackerRepository: TrackerRepository,
    private val trackerPoller: TrackerPoller,
    private val appNavigator: AppNavigator,
    private val trackerId: Long,
) : ViewModel(), AppNavigator by appNavigator{

    private val loading = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)
    private val tracker = trackerRepository.observeTracker(trackerId)
    private val trackerStates = trackerPoller.startPolling(listOf(trackerId))
    private val pollingErrors = trackerPoller.observeErrors() //TODO: Show error to user.

    val state: StateFlow<TrackerDetailsState> = combine(
        tracker,
        trackerStates,
        loading,
        errorMessage,
    ) { tracker, trackerStates, loading, errorMessage ->
        TrackerDetailsState(
            tracker = tracker,
            trackerState = trackerStates[trackerId],
            loading = loading,
            errorMessage = errorMessage
        )
    }.onStart { fetchData() }
        .stateIn(viewModelScope, vmSharingStarted, TrackerDetailsState())


    private fun fetchData() {
        viewModelScope.launch {
            loading.value = state.value.tracker == null
            suspendCatching(
                run = {
                    trackerRepository.fetchTracker(trackerId)
                    errorMessage.value = null
                },
                onException = { e -> errorMessage.value = e.message }
            )
            loading.value = false
        }
    }
}