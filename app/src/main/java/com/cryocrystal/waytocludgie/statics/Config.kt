package com.cryocrystal.waytocludgie.statics

/**
 * Created by destr on 27/02/2018.
 */
object Config {
    const val SHARED_PREF = "WcShPr"
    const val KEY_NEXT_UPDATE = "KeyNextUpdate"

    const val SANISETTES_CACHE_FILE_NAME = "Sanisettes.data"

    const val CACHE_DURATION = 1000L * 3600L * 24L // 24h
    const val POSITION_RETRY_STARTING_DELAY = 500L // 0.5 sec
    const val POSITION_MAX_RETRY_COUNT = 5
}