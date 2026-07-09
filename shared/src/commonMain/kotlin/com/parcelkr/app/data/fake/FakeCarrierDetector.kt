package com.parcelkr.app.data.fake

import com.parcelkr.app.domain.CarrierDetector
import com.parcelkr.app.domain.model.Carrier
import com.parcelkr.app.domain.model.CarrierGuess

class FakeCarrierDetector : CarrierDetector {
    override fun detect(trackingNumber: String): CarrierGuess {
        val digits = trackingNumber.filter { it.isDigit() }
        return when {
            digits.isEmpty() -> CarrierGuess(Carrier.UNKNOWN, confident = false)
            digits.length >= 12 -> CarrierGuess(Carrier.CJ, confident = true)
            else -> CarrierGuess(Carrier.CJ, confident = false)
        }
    }
}
