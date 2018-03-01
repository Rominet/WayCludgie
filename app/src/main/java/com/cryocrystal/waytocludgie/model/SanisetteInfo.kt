package com.cryocrystal.waytocludgie.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.cryocrystal.waytocludgie.R
import com.fasterxml.jackson.annotation.JsonIgnore

class SanisetteInfo(val objectId: Int,
                    val source: String,
                    val borough: String,
                    val streetName: String,
                    val streetNumber: String,
                    val administrator: String,
                    val lat: Double,
                    val lng: Double) : Parcelable {
    var openingHour: Int = -1
    var closingHour: Int = -1
    @JsonIgnore
    var distance: Float = 0f

    constructor(recordItem: SanisetteRecordItem) : this(recordItem.fields.objectid,
            recordItem.fields.source,
            recordItem.fields.arrondissement,
            recordItem.fields.nomVoie,
            recordItem.fields.numeroVoie,
            recordItem.fields.gestionnaire,
            recordItem.fields.position[0],
            recordItem.fields.position[1]) {

        val match = hourRegex.matchEntire(recordItem.fields.horairesOuverture)
        if (match != null && match.groups.size == 3) {
            val opening = match.groupValues[1].toInt()
            openingHour = if (opening == 24) 0 else opening // Replace 24 by 0 on openingHour
            val closing = match.groupValues[2].toInt()
            closingHour = if (closing < 12) closing + 12 else closing  // Add 12h when format is not 24h ... 6h - 1h
        } else {
            openingHour = -1
            closingHour = -1
        }
    }

    fun getOpeningHours(context: Context): String {
        return if (openingHour == 0 && closingHour == 24){
            context.getString(R.string.all_the_time)
        } else {
            context.getString(R.string.opening_hours_format,
                    if (openingHour == -1) '?' else openingHour,
                    if (closingHour == -1) '?' else closingHour)
        }
    }

    //---------- Parcelable methods ----------

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(objectId)
        writeString(source)
        writeString(borough)
        writeString(streetName)
        writeString(streetNumber)
        writeString(administrator)
        writeDouble(lat)
        writeDouble(lng)
    }

    companion object {
        private val hourRegex: Regex = """(\d{1,2}) h [-\/] (\d{1,2}).*""".toRegex()

        @JvmField
        val CREATOR: Parcelable.Creator<SanisetteInfo> = object : Parcelable.Creator<SanisetteInfo> {
            override fun createFromParcel(source: Parcel): SanisetteInfo = SanisetteInfo(source)
            override fun newArray(size: Int): Array<SanisetteInfo?> = arrayOfNulls(size)
        }
    }
}