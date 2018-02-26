package com.cryocrystal.waytocludgie.presenter

import com.cryocrystal.mvp.presenter.Presenter
import com.cryocrystal.mvp.rxutils.onIO
import com.cryocrystal.waytocludgie.api.SanisettesApiService
import com.cryocrystal.waytocludgie.model.SanisetteRecordItem

class MarkersPresenter(private val contract: MarkersContract) : Presenter(){

    val sanisetteApiServe by lazy {
        SanisettesApiService.create()
    }

    fun fetchMarkers(){
        link(sanisetteApiServe.getCompleteList()
                .onIO()
                .subscribe { response -> contract.onRecordsUpdated(response.records) })
    }
}

interface MarkersContract {
    fun onRecordsUpdated(records : List<SanisetteRecordItem>?)
}