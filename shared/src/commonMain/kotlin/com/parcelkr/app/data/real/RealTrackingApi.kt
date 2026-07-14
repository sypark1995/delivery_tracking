package com.parcelkr.app.data.real

import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.TrackingResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

private const val ENDPOINT = "https://apis.tracker.delivery/graphql"

private const val TRACK_QUERY = """
query Track(${'$'}carrierId: ID!, ${'$'}trackingNumber: String!) {
  track(carrierId: ${'$'}carrierId, trackingNumber: ${'$'}trackingNumber) {
    trackingNumber
    lastEvent {
      status { code name }
      time
      description
      contact { name phoneNumber }
    }
    events(first: 50) {
      edges {
        node {
          status { code name }
          time
          description
          location { name }
          contact { name phoneNumber }
        }
      }
    }
  }
}
"""

@Serializable
private data class GraphQLRequestBody(val query: String, val variables: JsonObject)

class RealTrackingApi(
    private val client: HttpClient,
    private val clientId: String,
    private val clientSecret: String,
) : TrackingApi {

    class TrackingNotFoundException(message: String) : Exception(message)

    override suspend fun track(trackingNumber: String, carrier: Carrier): TrackingResult {
        val variables = buildJsonObject {
            put("carrierId", JsonPrimitive(CarrierIds.idFor(carrier)))
            put("trackingNumber", JsonPrimitive(trackingNumber))
        }
        val response = client.post(ENDPOINT) {
            header("Authorization", "TRACKQL-API-KEY $clientId:$clientSecret")
            contentType(ContentType.Application.Json)
            setBody(GraphQLRequestBody(TRACK_QUERY, variables))
        }.body<TrackGraphQLResponse>()

        val trackInfo = response.data?.track
            ?: throw TrackingNotFoundException(
                response.errors?.firstOrNull()?.message
                    ?: "No tracking data found for $trackingNumber"
            )
        return TrackInfoMapper.toTrackingResult(trackInfo, carrier)
    }
}
