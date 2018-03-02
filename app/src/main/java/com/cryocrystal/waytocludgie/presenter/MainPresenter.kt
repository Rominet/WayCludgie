package com.cryocrystal.waytocludgie.presenter

import android.content.Context
import com.cryocrystal.mvp.presenter.Presenter
import com.cryocrystal.mvp.rxutils.onIO
import com.cryocrystal.waytocludgie.actions.SanisetteActionsHelper
import com.cryocrystal.waytocludgie.api.SanisettesApiService
import com.cryocrystal.waytocludgie.contractbehaviour.SanisettesCallback
import com.cryocrystal.waytocludgie.contractbehaviour.WebLoading
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.rxutils.CacheRequest
import com.cryocrystal.waytocludgie.rxutils.RequestedResponse
import com.cryocrystal.waytocludgie.rxutils.loadListFromCache
import com.cryocrystal.waytocludgie.rxutils.saveToCache
import com.cryocrystal.waytocludgie.statics.Config
import io.reactivex.rxkotlin.subscribeBy

class MainPresenter(context: Context, private val contract: MainContract) : Presenter() {

    val actionsHelper = SanisetteActionsHelper(context)

    private val sanisetteApiServe by lazy {
        SanisettesApiService.create()
    }

    val webObservable by lazy {
        sanisetteApiServe.getCompleteList().map { response -> response.records!!.map { SanisetteInfo(it) } }.saveToCache(context, Config.SANISETTES_CACHE_FILE_NAME)
    }

    val sanisettesObservable = CacheRequest.createFromConfig<List<SanisetteInfo>>(
            onCache = { loadListFromCache<SanisetteInfo>(context, Config.SANISETTES_CACHE_FILE_NAME, SanisetteInfo::class.java) },
            onWeb = { webObservable },
            context = context)
            .observable


    fun fetchInfo() {
        link(sanisettesObservable.subscribeBy(
                onNext = {
                    when (it) {
                        is RequestedResponse.Cache -> {
                            if (it.apiResponse == null) {
                                contract.onDisplayLoader()
                            } else {
                                actionsHelper.publishFromResultFromResponse(it.apiResponse)
                            }
                        }
                        is RequestedResponse.Remote -> actionsHelper.publishFromResultFromResponse(it.apiResponse)
                    }
                },
                onError = { contract.onWebError(it) }))

        link(actionsHelper.sanisettesObservable
                .subscribe {
                    actionsHelper.treatResults(it, contract)
                })
    }

    fun forceRefresh(){
        contract.onDisplayLoader()
        link(webObservable
                .onIO()
                .subscribeBy(
                onNext = { actionsHelper.publishFromResultFromResponse(it) },
                onError = { contract.onWebError(it) }))
    }
}

interface MainContract : WebLoading, SanisettesCallback {}