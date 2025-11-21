package com.blueprint.squaregps.feature.auth.data.impl

import com.blueprint.squaregps.feature.auth.data.api.AuthApi
import com.blueprint.squaregps.feature.auth.data.dto.AuthRequest
import com.blueprint.squaregps.feature.auth.domain.LoginDataSource


class LoginDataSourceImpl(
    private val authApi: AuthApi,
) : LoginDataSource {
    override suspend fun login(username: String, password: String): String {
        return authApi.auth(AuthRequest(username, password)).hash
    }
}