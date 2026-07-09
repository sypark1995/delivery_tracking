package com.parcelkr.app.state

import com.parcelkr.app.data.ParcelRepository
import com.parcelkr.app.domain.CarrierDetector
import com.parcelkr.app.domain.TrackingApi
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

    fun onInput(text: String) {
        _input.value = text
        _guess.value = if (text.isBlank()) null else detector.detect(text)
    }

    suspend fun confirmAdd(): Long? {
        val number = _input.value.trim()
        if (number.isBlank()) return null
        val carrier = detector.detect(number).carrier
        val result = api.track(number, carrier)
        return repo.add(number, carrier, result.itemName, result.status, result.etaText, result.progress)
    }
}
