package com.cryocrystal.waytocludgie.model

import com.fasterxml.jackson.annotation.JsonProperty

data class SanisettesResponse(@JsonProperty("nhits")
                              val nhits: Int = 0,
                              @JsonProperty("parameters")
                              val parameters: SanisetteParameters,
                              @JsonProperty("records")
                              val records: List<SanisetteRecordItem>?)