package com.blueprint.squaregps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueprint.squaregps.core.auth.SessionHashProvider
import com.blueprint.squaregps.navigation.AppNavigator
import com.blueprint.squaregps.navigation.LoginRoute
import com.blueprint.squaregps.navigation.TrackerListRoute
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionHashProvider: SessionHashProvider,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    init {
        viewModelScope.launch {
            sessionHashProvider.observeSessionHash().collect { value ->
                if (value == null) {
                    appNavigator.navigateTo(route = LoginRoute, popUpTo = TrackerListRoute)
                }
            }
        }
    }
}