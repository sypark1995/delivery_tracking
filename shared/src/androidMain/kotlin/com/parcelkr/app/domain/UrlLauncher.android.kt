package com.parcelkr.app.domain

import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidUrlLauncher(private val context: Context) : UrlLauncher {
    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openMapSearch(query: String) {
        val uri = Uri.parse("geo:0,0?q=" + Uri.encode(query))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
