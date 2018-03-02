package com.cryocrystal.waytocludgie.actions

import android.location.Location
import com.cryocrystal.mvp.rxutils.VariableCollection
import com.cryocrystal.waytocludgie.contractbehaviour.SanisettesCallback
import com.cryocrystal.waytocludgie.model.SanisetteInfo

class SanisetteActionsHelper {

    private var currentLocation: Location? = null
    private var showOpened = true
    private var showClosed = true
    private var showFavorites = true

    private val sanisettesVariable = VariableCollection.create(ArrayList<SanisetteInfo>())
    val sanisettesObservable = sanisettesVariable.observable

    fun treatResults(results: MutableCollection<SanisetteInfo>, contract: SanisettesCallback){
        val location = currentLocation
        if (location != null){

            results.forEach { info ->
                val dist = FloatArray(1)
                Location.distanceBetween(info.lat, info.lng, location.latitude, location.longitude, dist)
                info.distance = dist[0] }
        }
        // Filter results
        contract.onSanisettesUpdated(results.filter {
            (showOpened && it.opened) || (showClosed && !it.opened)
        })
    }


    fun publishFromResultFromResponse(sanisettes: List<SanisetteInfo>?) {
        if (sanisettes == null) {
            sanisettesVariable.clear()
        } else {
            sanisettesVariable.clear(false)
            sanisettesVariable.addAll(sanisettes)
        }
    }

    fun updateCurrentLocation(location: Location?) {
        currentLocation = location
        sanisettesVariable.publish()
    }


    fun filterOpened(show: Boolean){
        showOpened = show
        sanisettesVariable.publish()
    }

    fun filterClosed(show: Boolean){
        showClosed = show
        sanisettesVariable.publish()
    }

    fun filterFavorites(show: Boolean){
        showFavorites = show
        sanisettesVariable.publish()
    }
}