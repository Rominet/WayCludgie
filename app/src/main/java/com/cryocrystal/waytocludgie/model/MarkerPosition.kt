package com.cryocrystal.waytocludgie.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class MarkerPosition(@JsonProperty("coordinates") coordinates: List<Double>){
    val lng: Double = coordinates[0]
    val lat: Double = coordinates[1]
}