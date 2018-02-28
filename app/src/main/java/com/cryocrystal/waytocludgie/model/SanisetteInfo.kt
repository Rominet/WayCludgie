package com.cryocrystal.waytocludgie.model

class SanisetteInfo(recordItem: SanisetteRecordItem) {
    val objectId : Int = recordItem.fields.objectid
    val source: String = recordItem.fields.source
    val borough: String = recordItem.fields.arrondissement
    val streetName: String = recordItem.fields.nomVoie
    val streetNumber: String = recordItem.fields.numeroVoie
    val administrator: String = recordItem.fields.gestionnaire
    val lat: Double = recordItem.fields.position[0]
    val lng: Double = recordItem.fields.position[1]
    val openingHour: Int
    val closingHour: Int

    init {
        val match = hourRegex.matchEntire(recordItem.fields.horairesOuverture)
        if (match != null && match.groups.size >= 2){
            openingHour = match.groupValues[0].toInt()
            val closing = match.groupValues[1].toInt()
            closingHour = if (closing < 12) closing + 12 else closing  // Add 12h when format is not 24h ... 6h - 1h
        } else {
            openingHour = -1
            closingHour = -1
        }
    }

    companion object {
        private val hourRegex : Regex = "(\\d{1,2})".toRegex()
    }

}