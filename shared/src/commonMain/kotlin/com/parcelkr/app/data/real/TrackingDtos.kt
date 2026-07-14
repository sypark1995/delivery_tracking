package com.parcelkr.app.data.real

import kotlinx.serialization.Serializable

@Serializable
data class TrackGraphQLResponse(
    val data: TrackQueryData? = null,
    val errors: List<GraphQLErrorDto>? = null,
)

@Serializable
data class GraphQLErrorDto(val message: String)

@Serializable
data class TrackQueryData(val track: TrackInfoDto? = null)

@Serializable
data class TrackInfoDto(
    val trackingNumber: String,
    val lastEvent: TrackEventDto? = null,
    val events: TrackEventConnectionDto? = null,
)

@Serializable
data class TrackEventConnectionDto(val edges: List<TrackEventEdgeDto> = emptyList())

@Serializable
data class TrackEventEdgeDto(val node: TrackEventDto)

@Serializable
data class TrackEventDto(
    val status: TrackEventStatusDto,
    val time: String? = null,
    val description: String? = null,
    val location: LocationDto? = null,
    val contact: ContactInfoDto? = null,
)

@Serializable
data class TrackEventStatusDto(val code: String, val name: String? = null)

@Serializable
data class LocationDto(val name: String? = null)

@Serializable
data class ContactInfoDto(val name: String? = null, val phoneNumber: String? = null)
