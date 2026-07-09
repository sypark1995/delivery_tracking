package com.parcelkr.app.i18n

import kotlin.test.Test
import kotlin.test.assertEquals

class AppStringsTest {
    @Test fun en_and_ko_differ() {
        assertEquals("Your parcels", stringsFor(Lang.EN).homeTitle)
        assertEquals("내 택배", stringsFor(Lang.KO).homeTitle)
    }
    @Test fun lang_lookup_by_code() {
        assertEquals(Lang.VI, Lang.fromCode("vi"))
        assertEquals(Lang.EN, Lang.fromCode("xx"))
    }
    @Test fun every_lang_provides_non_blank_add_cta() {
        Lang.entries.forEach { assertEquals(false, stringsFor(it).addParcel.isBlank()) }
    }
}
