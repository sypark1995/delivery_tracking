package com.parcelkr.app.state

import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.domain.CarrierDetector
import com.parcelkr.app.domain.OrderEmailParser
import com.parcelkr.app.domain.TrackingApi
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.CarrierGuess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddModel(
    private val repo: ParcelRepository,
    private val detector: CarrierDetector,
    private val api: TrackingApi,
) {
    private val _input = MutableStateFlow("")
    val input: StateFlow<String> = _input.asStateFlow()

    private val _guess = MutableStateFlow<CarrierGuess?>(null)
    val guess: StateFlow<CarrierGuess?> = _guess.asStateFlow()

    private val _pasteFailed = MutableStateFlow(false)
    val pasteFailed: StateFlow<Boolean> = _pasteFailed.asStateFlow()

    private val _trackingFailed = MutableStateFlow(false)
    val trackingFailed: StateFlow<Boolean> = _trackingFailed.asStateFlow()

    private val _manualCarrier = MutableStateFlow<Carrier?>(null)
    val manualCarrier: StateFlow<Carrier?> = _manualCarrier.asStateFlow()

    fun onInput(text: String) {
        _input.value = text
        _guess.value = if (text.isBlank()) null else detector.detect(text)
        _pasteFailed.value = false
        _trackingFailed.value = false
        _manualCarrier.value = null
    }

    fun selectCarrier(carrier: Carrier) {
        _manualCarrier.value = carrier
    }

    /** Parses a pasted order-confirmation email and, if a tracking number is found, prefills [input]. */
    fun onPasteEmail(emailText: String) {
        val number = OrderEmailParser.extractTrackingNumber(emailText)
        if (number != null) {
            onInput(number)
        } else {
            _pasteFailed.value = true
        }
    }

    suspend fun confirmAdd(): Long? {
        val number = _input.value.trim()
        if (number.isBlank()) return null
        val carrier = _manualCarrier.value ?: detector.detect(number).carrier
        val result = try {
            val r = api.track(number, carrier)
            _trackingFailed.value = false
            r
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            _trackingFailed.value = true
            return null
        }
        val id = repo.add(number, carrier, result.itemName, result.status, result.etaText, result.progress)
        onInput("")
        return id
    }
}
