package com.cryocrystal.waytocludgie.contractbehaviour


interface Loading {
    fun onDisplayLoader()
}

interface WebLoading : Loading {
    fun onWebError(e: Throwable)
}