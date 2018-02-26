package com.cryocrystal.waytocludgie.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SanisetteParameters(@JsonProperty("timezone")
                      val timeZone: TimeZone,
                      @JsonProperty("format")
                      val format: String = "",
                      @JsonProperty("rows")
                      val rows: Int = 0,
                      @JsonProperty("dataset")
                      val dataset: List<String>?)