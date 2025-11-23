package com.blueprint.squaregps.feature.auth.ui.model

sealed interface LoginAction {
    object Login: LoginAction
    object DemoLogin: LoginAction
    data class SetFields(val username: String? = null, val password: String? = null): LoginAction
}