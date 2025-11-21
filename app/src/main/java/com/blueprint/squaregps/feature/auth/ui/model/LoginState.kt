package com.blueprint.squaregps.feature.auth.ui.model

data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val errorMessage: String? = null
)