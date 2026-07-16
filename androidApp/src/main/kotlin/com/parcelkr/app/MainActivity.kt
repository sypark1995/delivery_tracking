package com.parcelkr.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.parcelkr.app.ui.ParcelApp
import com.parcelkr.app.widget.EXTRA_CARRIER_NAME
import com.parcelkr.app.widget.EXTRA_TRACKING_NUMBER

const val ACTION_ADD_PARCEL = "com.parcelkr.app.action.ADD_PARCEL"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }
        val sharedText = if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
        val openAddDirectly = intent?.action == ACTION_ADD_PARCEL
        val widgetTrackingNumber = intent?.getStringExtra(EXTRA_TRACKING_NUMBER)
        val widgetCarrierName = intent?.getStringExtra(EXTRA_CARRIER_NAME)
        setContent {
            ParcelApp(
                initialSharedText = sharedText,
                openAddDirectly = openAddDirectly,
                initialDetailTrackingNumber = widgetTrackingNumber,
                initialDetailCarrierName = widgetCarrierName,
            )
        }
    }
}
