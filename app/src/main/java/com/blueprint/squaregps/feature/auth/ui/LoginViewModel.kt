package com.blueprint.squaregps.feature.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueprint.squaregps.core.exception.suspendCatching
import com.blueprint.squaregps.core.ui.vmSharingStarted
import com.blueprint.squaregps.feature.auth.domain.LoginRepository
import com.blueprint.squaregps.feature.auth.domain.model.LoginResult
import com.blueprint.squaregps.feature.auth.ui.model.LoginAction
import com.blueprint.squaregps.feature.auth.ui.model.LoginState
import com.blueprint.squaregps.navigation.AppNavigator
import com.blueprint.squaregps.navigation.LoginRoute
import com.blueprint.squaregps.navigation.TrackerListRoute
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

    private val loginFields = MutableStateFlow(LoginState.Fields())
    private val loading = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val state: StateFlow<LoginState> = combine(
        loginFields,
        loading,
        errorMessage,
    ) { loginFields, loading, errorMessage ->
        LoginState(
            fields = loginFields,
            loading = loading,
            errorMessage = errorMessage,
        )
    }.stateIn(viewModelScope, vmSharingStarted, LoginState())


    fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.DemoLogin -> demoLogin()
            is LoginAction.Login -> login()
            is LoginAction.SetFields -> setFields(action)
        }
    }

    private fun setFields(action: LoginAction.SetFields) {
        errorMessage.value = null
        loginFields.update {
            val username = action.username ?: it.username
            val password = action.password ?: it.password
            it.copy(username = username, password = password)
        }
    }

    private fun login(
        username: String = state.value.fields.username,
        password: String = state.value.fields.password
    ) = viewModelScope.launch {
        suspendCatching(
            run = {
                loading.value = true
                errorMessage.value = null
                val loginResult = loginRepository.login(username, password)
                when (loginResult) {
                    is LoginResult.Success -> {
                        appNavigator.navigateTo(route = TrackerListRoute, popUpTo = LoginRoute)
                    }

                    is LoginResult.Failed -> {
                        errorMessage.value = loginResult.message
                    }
                }
            },
            onException = { e ->
                errorMessage.value = e.message
            }
        )
        loading.value = false
    }

    private fun demoLogin() = login("demo-eu@navixy.com", "123456")

}