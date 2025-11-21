package com.blueprint.squaregps.feature.auth.domain.model

sealed interface LoginResult {
    object Success : LoginResult
    class Failed(val e: Exception) : LoginResult
}