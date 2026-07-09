package com.parcelkr.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.parcelkr.app.domain.model.DeliveryStatus
import com.parcelkr.app.i18n.AppStrings
import com.parcelkr.app.ui.theme.StatusColors
import com.parcelkr.app.ui.theme.StatusPalette

fun statusColorsFor(status: DeliveryStatus): StatusColors =
    StatusPalette[status.name] ?: StatusPalette.getValue("RECEIVED")

fun statusIcon(status: DeliveryStatus): ImageVector = when (status) {
    DeliveryStatus.RECEIVED -> Icons.Outlined.Inbox
    DeliveryStatus.IN_TRANSIT -> Icons.Outlined.LocalShipping
    DeliveryStatus.OUT_FOR_DELIVERY -> Icons.Outlined.LocalShipping
    DeliveryStatus.DELIVERED -> Icons.Outlined.CheckCircle
    DeliveryStatus.FAILED -> Icons.Outlined.Warning
    DeliveryStatus.DELAYED -> Icons.Outlined.Schedule
}

fun statusLabel(status: DeliveryStatus, s: AppStrings): String = when (status) {
    DeliveryStatus.RECEIVED -> "Received"
    DeliveryStatus.IN_TRANSIT -> "In transit"
    DeliveryStatus.OUT_FOR_DELIVERY -> "Out for delivery"
    DeliveryStatus.DELIVERED -> s.delivered
    DeliveryStatus.FAILED -> "Delivery failed"
    DeliveryStatus.DELAYED -> "Delayed"
}
