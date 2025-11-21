package com.blueprint.squaregps.feature.tracker.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blueprint.squaregps.feature.tracker.domain.model.Tracker
import com.blueprint.squaregps.feature.tracker.ui.model.TrackerAction
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerListScreen(
    viewModel: TrackerListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = state.list.isEmpty()
    val refreshing = state.refreshing

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trackers") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                PullToRefreshBox(
                    isRefreshing = refreshing,
                    onRefresh = {
                        viewModel.handleAction(TrackerAction.RefreshTrackerList)
                    },
                    state = rememberPullToRefreshState(),
                ) {
                    LazyColumn {
                        items(state.list) { tracker ->
                            TrackerItem(
                                tracker = tracker,
                                onClick = { viewModel.handleAction(TrackerAction.OpenTrackerDetails(tracker)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrackerItem(
    tracker: Tracker,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = tracker.label,
                style = MaterialTheme.typography.titleMedium
            )
            if (!tracker.model.isNullOrEmpty()) {
                Text(
                    text = tracker.model,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

