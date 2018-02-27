package com.cryocrystal.waytocludgie.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SanisetteFields(@JsonProperty("gestionnaire")
                           val gestionnaire: String = "",
                           @JsonProperty("arrondissement")
                           val arrondissement: String = "",
                           @JsonProperty("identifiant")
                           val identifiant: String = "",
                           @JsonProperty("geom_x_y")
                           val position: List<Double>,
                           @JsonProperty("x")
                           val x: Double = 0.0,
                           @JsonProperty("y")
                           val y: Double = 0.0,
                           @JsonProperty("numero_voie")
                           val numeroVoie: String = "",
                           @JsonProperty("source")
                           val source: String = "",
                           @JsonProperty("horaires_ouverture")
                           val horairesOuverture: String = "",
                           @JsonProperty("objectid")
                           val objectid: Int = 0,
                           @JsonProperty("nom_voie")
                           val nomVoie: String = "") {
    val lat : Double = position[0]
    val lng : Double = position[1]
}