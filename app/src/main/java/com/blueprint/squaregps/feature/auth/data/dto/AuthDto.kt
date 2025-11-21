package com.blueprint.squaregps.feature.auth.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class AuthRequest(val login: String, val password: String)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val hash: String,
    val status: Status? = null
) {
    @Serializable
    data class Status(val code: Int, val description: String? = null)
}