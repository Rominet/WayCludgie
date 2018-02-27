package com.cryocrystal.waytocludgie.contractbehaviour


interface Loading {
    fun onDisplayLoader()
}

interface DataLoading<in T> : Loading {
    fun onSuccess(data : T)
    fun onWebError(e: Throwable)
}