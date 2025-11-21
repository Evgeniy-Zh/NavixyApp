package com.blueprint.squaregps.feature.auth.data.impl

import android.content.Context
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.blueprint.squaregps.core.auth.SessionHashMutableProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SessionHashProviderImpl(private val context: Context) : SessionHashMutableProvider {

    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.dataStoreFile("auth.preferences_pb") }
    )

    private val keyHash = stringPreferencesKey("session_hash")

    private val _sessionHash = dataStore.data
        .map { it[keyHash] }
        .catch { emit(null) }

    override suspend fun updateSessionHash(sessionHash: String) {
        dataStore.edit { it[keyHash] = sessionHash }
    }

    override suspend fun clearSessionHash() {
        dataStore.edit { it.remove(keyHash) }
    }

    override fun observeSessionHash(): Flow<String?> {
        return _sessionHash.distinctUntilChanged()
    }

    override suspend fun getSessionHash(): String? {
        return _sessionHash.first()
    }


}