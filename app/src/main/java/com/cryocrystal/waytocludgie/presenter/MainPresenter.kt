package com.cryocrystal.waytocludgie.presenter

import android.content.Context
import android.location.Location
import com.cryocrystal.mvp.presenter.Presenter
import com.cryocrystal.mvp.rxutils.VariableCollection
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

    private var currentLocation: Location? = null

    val sanisettesObservable = CacheRequest.createFromConfig<List<SanisetteInfo>>(
            onCache = { loadListFromCache<SanisetteInfo>(context, Config.SANISETTES_CACHE_FILE_NAME, SanisetteInfo::class.java) },
            onWeb = { sanisetteApiServe.getCompleteList().map { response -> response.records!!.map { SanisetteInfo(it) } }.saveToCache(context, Config.SANISETTES_CACHE_FILE_NAME) },
            context = context)
            .observable

    val sanisettesVariable = VariableCollection.create(ArrayList<SanisetteInfo>())

    private val sanisetteApiServe by lazy {
        SanisettesApiService.create()
    }

    fun fetchInfo() {
        link(sanisettesObservable.subscribeBy(
                onNext = {
                    when (it) {
                        is RequestedResponse.Cache -> {
                            if (it.apiResponse == null) {
                                contract.onDisplayLoader()
                            } else {
                                publishFromResultFromResponse(it.apiResponse)
                            }
                        }
                        is RequestedResponse.Remote -> publishFromResultFromResponse(it.apiResponse)
                    }
                },
                onError = { contract.onWebError(it) }))

        link(sanisettesVariable.observable
                .subscribe { results ->
                    val location = currentLocation
                    if (location != null){

                        results.forEach { info ->
                            val dist = FloatArray(1)
                            Location.distanceBetween(info.lat, info.lng, location.latitude, location.longitude, dist)
                            info.distance = dist[0] }
                    }
            contract.onSanisettesUpdated(results.toList())
        })
    }

    fun updateInfosWithLocation(location: Location?) {
        currentLocation = location
        sanisettesVariable.publish()

    }

    private fun publishFromResultFromResponse(sanisettes: List<SanisetteInfo>?) {
        if (sanisettes == null) {
            sanisettesVariable.clear()
        } else {
            sanisettesVariable.clear(false)
            sanisettesVariable.addAll(sanisettes)
        }
    }
}

interface MainContract : WebLoading, SanisettesCallback {}