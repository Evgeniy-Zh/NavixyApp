package com.blueprint.squaregps.feature.auth.data.api

import com.blueprint.squaregps.feature.auth.data.dto.AuthRequest
import com.blueprint.squaregps.feature.auth.data.dto.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApi(
    private val client: HttpClient,
) {

    suspend fun auth(request: AuthRequest): AuthResponse {
        return client.post("user/auth") {
            setBody(request)
        }.body<AuthResponse>()
    }

}