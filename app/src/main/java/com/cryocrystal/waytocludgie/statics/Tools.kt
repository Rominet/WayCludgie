package com.cryocrystal.waytocludgie.statics

import android.content.Context
import com.cryocrystal.waytocludgie.R
import java.util.*


object Tools {

    private val calendar by lazy {
        Calendar.getInstance()
    }

    fun formatDistance(context: Context, distanceInMeters: Float): String {
        if (distanceInMeters < 1000) {
            return context.getString(R.string.meters_formatted, distanceInMeters)
        }
        return context.getString(R.string.kilometers_formatted, distanceInMeters / 1000)
    }

    val nowCalendar: Calendar
        get() {
            calendar.time = Date()
            return calendar
        }
}