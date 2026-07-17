package com.parcelkr.app.data.real

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track17RegisterItem(val number: String)

@Serializable
data class Track17TrackItem(val number: String)

@Serializable
data class Track17GetTrackInfoResponse(val code: Int, val data: Track17Data? = null)

@Serializable
data class Track17Data(val accepted: List<Track17Accepted> = emptyList())

@Serializable
data class Track17Accepted(
    val number: String,
    @SerialName("track_info") val trackInfo: Track17TrackInfo? = null,
)

@Serializable
data class Track17TrackInfo(
    @SerialName("latest_status") val latestStatus: Track17LatestStatus? = null,
    val tracking: Track17Tracking? = null,
)

@Serializable
data class Track17LatestStatus(val status: String? = null)

@Serializable
data class Track17Tracking(val providers: List<Track17Provider> = emptyList())

@Serializable
data class Track17Provider(
    val provider: Track17ProviderInfo? = null,
    val events: List<Track17Event> = emptyList(),
)

@Serializable
data class Track17ProviderInfo(val name: String? = null)

@Serializable
data class Track17Event(
    @SerialName("time_iso") val timeIso: String? = null,
    val description: String? = null,
    val location: String? = null,
    val stage: String? = null,
)
