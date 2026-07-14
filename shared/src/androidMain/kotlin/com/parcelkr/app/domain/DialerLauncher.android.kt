package com.parcelkr.app.domain

import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidDialerLauncher(private val context: Context) : DialerLauncher {
    override fun launch(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
