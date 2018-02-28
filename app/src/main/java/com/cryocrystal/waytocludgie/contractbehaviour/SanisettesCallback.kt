package com.cryocrystal.waytocludgie.contractbehaviour

import com.cryocrystal.waytocludgie.model.SanisetteInfo

interface SanisettesCallback {
    fun onSanisettesUpdated(sanisettes: List<SanisetteInfo>?)
}