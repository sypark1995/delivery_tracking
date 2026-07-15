package com.parcelkr.app.domain

interface UrlLauncher {
    fun openUrl(url: String)
    fun openMapSearch(query: String)
}
