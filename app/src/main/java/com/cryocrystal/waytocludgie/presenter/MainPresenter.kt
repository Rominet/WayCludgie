package com.cryocrystal.waytocludgie.presenter

import android.content.Context
import com.cryocrystal.mvp.presenter.Presenter
import com.cryocrystal.waytocludgie.api.SanisettesApiService
import com.cryocrystal.waytocludgie.contractbehaviour.DataLoading
import com.cryocrystal.waytocludgie.model.SanisetteRecordItem
import com.cryocrystal.waytocludgie.model.SanisettesResponse
import com.cryocrystal.waytocludgie.rxutils.CacheRequest
import com.cryocrystal.waytocludgie.rxutils.loadFromCache
import com.cryocrystal.waytocludgie.rxutils.saveToCache
import com.cryocrystal.waytocludgie.rxutils.treatSuccess
import com.cryocrystal.waytocludgie.statics.Config
import io.reactivex.rxkotlin.subscribeBy

class MainPresenter(private val contract: MainContract) : Presenter() {

    private val sanisetteApiServe by lazy {
        SanisettesApiService.create()
    }

    fun fetchMarkers(context: Context) {
        link(CacheRequest.createFromConfig<SanisettesResponse>(
                onCache = { loadFromCache<SanisettesResponse>(context, Config.SANISETTES_CACHE_FILE_NAME) },
                onWeb = { sanisetteApiServe.getCompleteList().saveToCache(context, Config.SANISETTES_CACHE_FILE_NAME) },
                context = context)
                .observable
                .subscribeBy(onNext = { it.treatSuccess(contract) },
                        onError = { contract.onWebError(it) }))
    }
}

interface MainContract : DataLoading<SanisettesResponse>