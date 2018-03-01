package com.cryocrystal.waytocludgie.statics

import android.content.Context
import com.cryocrystal.waytocludgie.R


object Tools {

    fun formatDistance(context: Context, distanceInMeters: Float): String {
        if (distanceInMeters < 1000){
            return context.getString(R.string.meters_formatted, distanceInMeters)
        }
        return context.getString(R.string.kilometers_formatted, distanceInMeters / 1000)
    }
}