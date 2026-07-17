package com.parcelkr.app.data.real

import com.parcelkr.app.domain.OverseasTrackingApi
import com.parcelkr.app.domain.model.OverseasTrackingResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val REGISTER_ENDPOINT = "https://api.17track.net/track/v2.2/register"
private const val GET_TRACK_INFO_ENDPOINT = "https://api.17track.net/track/v2.2/gettrackinfo"

class Track17Api(
    private val client: HttpClient,
    private val apiKey: String,
) : OverseasTrackingApi {

    class OverseasTrackingNotFoundException(message: String) : Exception(message)

    override suspend fun register(trackingNumber: String) {
        // 17TRACK rejects re-registering an already-registered number with a harmless per-item
        // error inside "rejected" (not an HTTP failure) — safe to ignore the response entirely.
        client.post(REGISTER_ENDPOINT) {
            header("17token", apiKey)
            contentType(ContentType.Application.Json)
            setBody(listOf(Track17RegisterItem(trackingNumber)))
        }
    }

    override suspend fun track(trackingNumber: String): OverseasTrackingResult {
        val response = client.post(GET_TRACK_INFO_ENDPOINT) {
            header("17token", apiKey)
            contentType(ContentType.Application.Json)
            setBody(listOf(Track17TrackItem(trackingNumber)))
        }.body<Track17GetTrackInfoResponse>()

        val accepted = response.data?.accepted?.firstOrNull()
            ?: throw OverseasTrackingNotFoundException("No overseas tracking data found for $trackingNumber")
        return Track17Mapper.toOverseasResult(accepted)
    }
}
