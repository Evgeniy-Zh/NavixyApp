package com.blueprint.squaregps.feature.tracker.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackerListResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("list") val list: List<TrackerDto>? = null,
)

@Serializable
data class TrackerResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("value") val tracker: TrackerDto,
)

@Serializable
data class TrackerDto(
    @SerialName("id") val id: Long,
    @SerialName("label") val label: String,
    @SerialName("group_id") val groupId: Int? = null,
    @SerialName("source") val source: SourceDto? = null,
    @SerialName("tag_bindings") val tagBindings: List<String>? = null,
    @SerialName("clone") val clone: Boolean? = null
) {
    @Serializable
    data class SourceDto(
        @SerialName("id") val id: Int,
        @SerialName("device_id") val deviceId: Long? = null,
        @SerialName("model") val model: String? = null,
        @SerialName("creation_date") val creationDate: String? = null,
        @SerialName("blocked") val blocked: Boolean? = null,
        @SerialName("tariff_id") val tariffId: Int? = null,
        @SerialName("tariff_end_date") val tariffEndDate: String? = null,
        @SerialName("phone") val phone: String? = null,
        @SerialName("status_listing_id") val statusListingId: Int? = null
    )
}