package com.blueprint.squaregps.feature.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueprint.squaregps.core.exception.suspendCatching
import com.blueprint.squaregps.core.ui.vmSharingStarted
import com.blueprint.squaregps.feature.auth.domain.LoginRepository
import com.blueprint.squaregps.feature.auth.ui.model.LoginAction
import com.blueprint.squaregps.feature.auth.ui.model.LoginState
import com.blueprint.squaregps.navigation.AppNavigator
import com.blueprint.squaregps.navigation.LoginRoute
import com.blueprint.squaregps.navigation.TrackerListRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    private val loading = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val state: StateFlow<LoginState> = combine(
        _state,
        loading,
        errorMessage,
    ) { state, loading, errorMessage ->
        state.copy(
            loading = loading,
            errorMessage = errorMessage,
        )
    }.stateIn(viewModelScope, vmSharingStarted, LoginState())


    fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.DemoLogin -> demoLogin()
            is LoginAction.Login -> login()
        }
    }

    private fun login() = viewModelScope.launch {
        errorMessage.value = "Not Implemented"
        delay(2_000)
        errorMessage.value = null
    }

    private fun demoLogin() = viewModelScope.launch {
        suspendCatching(
            run = {
                loading.value = true
                loginRepository.login("demo-eu@navixy.com", "123456")
                appNavigator.navigateTo(route = TrackerListRoute, popUpTo = LoginRoute)
            },
            onException = { e ->
                loading.value = false
                errorMessage.value = e.message
            }
        )
    }
}