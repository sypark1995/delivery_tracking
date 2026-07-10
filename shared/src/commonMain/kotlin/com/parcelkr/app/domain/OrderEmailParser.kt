package com.parcelkr.app.domain

/**
 * Extracts a plausible courier tracking number from raw order-confirmation email text
 * (Coupang / AliExpress / Amazon style). Pure client-side regex heuristic — no network calls.
 */
object OrderEmailParser {
    private const val MIN_DIGITS = 10
    private const val MAX_DIGITS = 16

    // A run of digits, allowed to contain hyphens/spaces as group separators, anchored on digits
    // at both ends so trailing punctuation (":" "," etc.) is never absorbed into the match.
    private const val NUMBER_BODY = """[0-9][0-9\- ]{8,20}[0-9]"""

    // Common tracking/invoice labels across Korean and English order emails, immediately
    // followed (same line) by the candidate number.
    private val labelRegex = Regex(
        """(?i)(?:tracking\s*(?:number|no\.?|#|code)?|invoice\s*(?:no\.?|number)?|송장\s*번호|운송장\s*번호)\s*[:#\-]?\s*($NUMBER_BODY)""",
    )

    // A line that consists solely of a digit run (with optional hyphen/space separators) —
    // matches unlabeled but clearly-a-code lines, e.g. bare Amazon tracking codes.
    private val standaloneLineRegex = Regex("""^$NUMBER_BODY$""")

    fun extractTrackingNumber(emailText: String): String? {
        labelRegex.find(emailText)?.let { match ->
            val digits = match.groupValues[1].filter(Char::isDigit)
            if (digits.length in MIN_DIGITS..MAX_DIGITS) return digits
        }

        emailText.lineSequence()
            .map { it.trim() }
            .firstOrNull { standaloneLineRegex.matches(it) }
            ?.let { line ->
                val digits = line.filter(Char::isDigit)
                if (digits.length in MIN_DIGITS..MAX_DIGITS) return digits
            }

        return null
    }
}
