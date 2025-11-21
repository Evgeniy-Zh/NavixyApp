package com.blueprint.squaregps.feature.tracker.data.api

import com.blueprint.squaregps.core.auth.SessionHashProvider
import com.blueprint.squaregps.feature.tracker.data.dto.TrackerListResponse
import com.blueprint.squaregps.feature.tracker.data.dto.TrackerResponse
import com.blueprint.squaregps.feature.tracker.data.dto.TrackerStateRequest
import com.blueprint.squaregps.feature.tracker.data.dto.TrackerStatesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TrackerApi(
    private val client: HttpClient,
    private val sessionHashProvider: SessionHashProvider,
) {

    suspend fun getTrackerList(): TrackerListResponse =
        client.post("tracker/list") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("hash" to sessionHashProvider.getSessionHash()))
        }.body<TrackerListResponse>()

    suspend fun getTracker(trackerId: Long): TrackerResponse {
        val sessionHash = sessionHashProvider.getSessionHash() ?: ""
        return client.get("tracker/read") {
            url {
                parameters.append("hash", sessionHash)
                parameters.append("tracker_id", trackerId.toString())
            }
        }.body<TrackerResponse>()
    }

    suspend fun getTrackerStates(request: TrackerStateRequest): TrackerStatesResponse =
        client.post("tracker/get_states") {
            setBody(
                request.copy(hash = sessionHashProvider.getSessionHash())
            )
        }.body<TrackerStatesResponse>()

}