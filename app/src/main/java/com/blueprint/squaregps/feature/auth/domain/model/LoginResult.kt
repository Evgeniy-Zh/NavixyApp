package com.blueprint.squaregps.feature.auth.domain.model

sealed interface LoginResult {
    object Success : LoginResult
    class Failed(val message: String, val e: Exception? = null) : LoginResult
}