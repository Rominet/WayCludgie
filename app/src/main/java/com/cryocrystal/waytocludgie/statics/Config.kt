package com.cryocrystal.waytocludgie.statics

object Config {
    const val SHARED_PREF = "WcShPr"
    const val KEY_NEXT_UPDATE = "KeyNextUpdate"
    const val KEY_FAVORITES = "KeyFavorites"

    const val SANISETTES_CACHE_FILE_NAME = "Sanisettes.data"

    const val CACHE_DURATION = 1000L * 3600L * 24L // 24h
    const val POSITION_RETRY_STARTING_DELAY = 500L // 0.5 sec
    const val POSITION_MAX_RETRY_COUNT = 5

    const val MINIMUM_LOADING_TIME = 3000L // 3 sec
}