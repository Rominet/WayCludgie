package com.cryocrystal.waytocludgie.model

class SanisetteInfo(val objectId: Int,
                    val source: String,
                    val borough: String,
                    val streetName: String,
                    val streetNumber: String,
                    val administrator: String,
                    val lat: Double,
                    val lng: Double) {
    var openingHour: Int = -1
    var closingHour: Int = -1

    constructor(recordItem: SanisetteRecordItem) : this(recordItem.fields.objectid,
            recordItem.fields.source,
            recordItem.fields.arrondissement,
            recordItem.fields.nomVoie,
            recordItem.fields.numeroVoie,
            recordItem.fields.gestionnaire,
            recordItem.fields.position[0],
            recordItem.fields.position[1]){

        val match = hourRegex.matchEntire(recordItem.fields.horairesOuverture)
        if (match != null && match.groups.size == 3){
            openingHour = match.groupValues[1].toInt()
            val closing = match.groupValues[2].toInt()
            closingHour = if (closing < 12) closing + 12 else closing  // Add 12h when format is not 24h ... 6h - 1h
        } else {
            openingHour = -1
            closingHour = -1
        }
    }

    companion object {
        private val hourRegex : Regex = """(\d{1,2}) h [-\/] (\d{1,2}).*""".toRegex()
    }

}