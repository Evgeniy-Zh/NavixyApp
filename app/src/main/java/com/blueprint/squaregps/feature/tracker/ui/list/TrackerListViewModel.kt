package com.blueprint.squaregps.feature.tracker.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueprint.squaregps.core.exception.suspendCatching
import com.blueprint.squaregps.core.ui.vmSharingStarted
import com.blueprint.squaregps.feature.tracker.domain.TrackerRepository
import com.blueprint.squaregps.feature.tracker.domain.model.Tracker
import com.blueprint.squaregps.feature.tracker.ui.model.TrackerAction
import com.blueprint.squaregps.feature.tracker.ui.model.TrackerListState
import com.blueprint.squaregps.navigation.AppNavigator
import com.blueprint.squaregps.navigation.TrackerDetailsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackerListViewModel(
    private val trackerRepository: TrackerRepository,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val trackers = trackerRepository.observeTrackers()
    private val refreshing = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val state = combine(
        trackers,
        refreshing,
        errorMessage,
    ) { trackers, refreshing, errorMessage ->
        TrackerListState(
            list = trackers,
            refreshing = refreshing,
            errorMessage = errorMessage,
        )
    }.onStart { fetchTrackers(showRefreshing = false) }
        .stateIn(viewModelScope, vmSharingStarted, TrackerListState())


    fun handleAction(action: TrackerAction) {
        when (action) {
            is TrackerAction.OpenTrackerDetails -> toTrackerDetails(action.tracker)
            is TrackerAction.RefreshTrackerList -> fetchTrackers(showRefreshing = true)
        }
    }

    private fun fetchTrackers(showRefreshing: Boolean) = viewModelScope.launch {
        refreshing.value = showRefreshing
        suspendCatching(
            run = {
                trackerRepository.fetchTrackers()
                errorMessage.value = null
            },
            onException = {
                errorMessage.value = it.message
            }
        )
        refreshing.value = false
    }

    private fun toTrackerDetails(tracker: Tracker) {
        appNavigator.navigateTo(TrackerDetailsRoute(tracker.id))
    }

}