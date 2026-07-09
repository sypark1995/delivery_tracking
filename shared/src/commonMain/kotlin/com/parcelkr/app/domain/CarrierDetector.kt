package com.parcelkr.app.domain

import com.parcelkr.app.domain.model.CarrierGuess

interface CarrierDetector {
    fun detect(trackingNumber: String): CarrierGuess
}
