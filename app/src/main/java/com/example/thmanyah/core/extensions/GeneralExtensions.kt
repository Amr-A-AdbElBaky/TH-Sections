package com.example.thmanyah.core.extensions

import android.content.Context
import com.example.thmanyah.R

fun Long.formatDurationWitQualifiers(context: Context): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60

    return if (hours > 0) {
        // Format as "HH:MM" if hours are present
     context.getString(R.string.label_hour_qualifier, hours) +
          context.getString(R.string.label_minute_qualifier, minutes)
    } else {
        // Format as "MM" if no hours
        context.getString(R.string.label_minute_qualifier, minutes)
    }
}