package com.parcelkr.app

import com.parcelkr.app.i18n.Lang
import java.util.Locale

actual fun deviceLang(): Lang = Lang.fromCode(Locale.getDefault().language)
