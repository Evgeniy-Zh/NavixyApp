package com.blueprint.squaregps.feature.auth.domain

import com.blueprint.squaregps.core.auth.SessionHashMutableProvider
import com.blueprint.squaregps.core.exception.UnauthorizedException
import com.blueprint.squaregps.feature.auth.domain.model.LoginResult

class LoginRepository(
    private val loginDataSource: LoginDataSource,
    private val sessionHashMutableProvider: SessionHashMutableProvider,
) {

    suspend fun login(username: String, password: String): LoginResult {
        return try {
            val hash = loginDataSource.login(username, password)
            sessionHashMutableProvider.updateSessionHash(hash)
            LoginResult.Success
        } catch (e: UnauthorizedException) {
            LoginResult.Failed(e.message?: "Unknown error", e)
        }
    }

}