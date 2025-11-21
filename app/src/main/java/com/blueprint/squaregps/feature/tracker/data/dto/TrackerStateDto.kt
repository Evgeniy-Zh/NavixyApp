package com.blueprint.squaregps.feature.tracker.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackerStateRequest(
    @SerialName("hash") val hash: String? = null,
    @SerialName("trackers") val trackerIds: List<Long> = emptyList(),
)

@Serializable
data class TrackerStatesResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("user_time") val userTime: String? = null,
    @SerialName("states") val states: Map<Long, StateDto>? = null,
    @SerialName("status") val status: Status? = null
) {
    @Serializable
    data class Status(
        @SerialName("code") val code: Int,
        @SerialName("description") val description: String? = null
    )
}

@Serializable
data class StateDto(
    @SerialName("source_id") val sourceId: Int,
    @SerialName("gps") val gps: GpsData? = null,
    @SerialName("connection_status") val connectionStatus: String? = null,
    @SerialName("movement_status") val movementStatus: String? = null,
    @SerialName("movement_status_update") val movementStatusUpdate: String? = null,
    @SerialName("ignition") val ignition: Boolean? = null,
    @SerialName("ignition_update") val ignitionUpdate: String? = null,
    @SerialName("inputs") val inputs: List<Boolean>? = null,
    @SerialName("inputs_update") val inputsUpdate: String? = null,
    @SerialName("outputs") val outputs: List<Boolean>? = null,
    @SerialName("outputs_update") val outputsUpdate: String? = null,
    @SerialName("last_update") val lastUpdate: String? = null,
    @SerialName("gsm") val gsm: GsmData? = null,
    @SerialName("battery_level") val batteryLevel: Int? = null,
    @SerialName("battery_update") val batteryUpdate: String? = null,
    @SerialName("additional") val additional: Map<String, AdditionalData>? = null,
    @SerialName("actual_track_update") val actualTrackUpdate: String? = null
) {
    @Serializable
    data class GpsData(
        @SerialName("updated") val updated: String? = null,
        @SerialName("signal_level") val signalLevel: Int? = null,
        @SerialName("location") val location: Location? = null,
        @SerialName("heading") val heading: Int? = null,
        @SerialName("speed") val speed: Int? = null,
        @SerialName("alt") val alt: Int? = null
    )

    @Serializable
    data class Location(
        @SerialName("lat") val lat: Double,
        @SerialName("lng") val lng: Double
    )

    @Serializable
    data class GsmData(
        @SerialName("updated") val updated: String? = null,
        @SerialName("signal_level") val signalLevel: Int? = null,
        @SerialName("network_name") val networkName: String? = null,
        @SerialName("roaming") val roaming: Boolean? = null
    )

    @Serializable
    data class AdditionalData(
        @SerialName("value") val value: String? = null,
        @SerialName("updated") val updated: String? = null
    )
}