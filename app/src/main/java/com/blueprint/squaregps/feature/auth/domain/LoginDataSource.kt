package com.blueprint.squaregps.feature.auth.domain

interface LoginDataSource {
    suspend fun login(username: String, password: String): String
}