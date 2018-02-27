package com.cryocrystal.waytocludgie.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SanisetteRecordItem(@JsonProperty("recordid")
                       val recordid: String = "",
                       @JsonProperty("datasetid")
                       val datasetid: String = "",
                       @JsonProperty("fields")
                       val fields: SanisetteFields,
                       @JsonProperty("record_timestamp")
                       val recordTimestamp: String = "")