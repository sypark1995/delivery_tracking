package com.parcelkr.app.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OrderEmailParserTest {

    @Test fun coupang_style_labeled_tracking_number_amid_date_and_price_noise() {
        val email = """
            [쿠팡] 주문이 완료되었습니다.
            주문번호: CP2026070912345
            주문일자: 2026-07-09
            결제금액: ${'$'}129.99
            운송장번호: 1234-5678-9012
            택배사: CJ대한통운
            감사합니다.
        """.trimIndent()

        assertEquals("123456789012", OrderEmailParser.extractTrackingNumber(email))
    }

    @Test fun amazon_style_unlabeled_standalone_numeric_code() {
        val email = """
            Your package has shipped!
            Order date: 2026-07-08
            Amazon.com order of "Wireless Mouse"
            Estimated delivery: July 12, 2026
            Total: ${'$'}24.99

            657606146365

            Track your package for updates.
        """.trimIndent()

        assertEquals("657606146365", OrderEmailParser.extractTrackingNumber(email))
    }

    @Test fun plain_prose_with_no_tracking_number_returns_null() {
        val email = "Hi there, thanks for shopping with us. Your order is being processed " +
            "and will arrive soon. Have a great day!"

        assertNull(OrderEmailParser.extractTrackingNumber(email))
    }

    @Test fun date_and_price_noise_without_real_tracking_number_returns_null() {
        val email = """
            주문이 완료되었습니다.
            주문일자: 2026-07-09
            결제금액: ${'$'}129.99
            감사합니다.
        """.trimIndent()

        assertNull(OrderEmailParser.extractTrackingNumber(email))
    }

    @Test fun aliexpress_style_invoice_label_with_hyphenated_number() {
        val email = """
            AliExpress Order Confirmation
            Order date: 2026-07-01
            Item price: ${'$'}9.99
            Invoice No: 1122-3344-5566-77
            Carrier: AliExpress Standard Shipping
        """.trimIndent()

        assertEquals("11223344556677", OrderEmailParser.extractTrackingNumber(email))
    }
}
