package com.cryocrystal.waytocludgie.model

import com.fasterxml.jackson.annotation.JsonProperty

data class SanisetteRecordItem(@JsonProperty("recordid")
                       val recordid: String = "",
                       @JsonProperty("datasetid")
                       val datasetid: String = "",
                       @JsonProperty("geometry")
                       val geometry: MarkerPosition,
                       @JsonProperty("fields")
                       val fields: SanisetteFields,
                       @JsonProperty("record_timestamp")
                       val recordTimestamp: String = "")