package com.blueprint.squaregps.core.auth

import kotlinx.coroutines.flow.Flow

interface SessionHashProvider {
    fun observeSessionHash() : Flow<String?>
    suspend fun getSessionHash() : String?
}

interface SessionHashMutableProvider : SessionHashProvider {
    suspend fun updateSessionHash(sessionHash: String)
    suspend fun clearSessionHash()
}