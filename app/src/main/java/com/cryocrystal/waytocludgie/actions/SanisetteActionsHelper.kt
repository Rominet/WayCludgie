package com.cryocrystal.waytocludgie.actions

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.support.v4.content.ContextCompat
import com.cryocrystal.mvp.rxutils.VariableCollection
import com.cryocrystal.waytocludgie.actions.SanisetteActionsHelper.SortOptions.*
import com.cryocrystal.waytocludgie.contractbehaviour.SanisettesCallback
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.statics.Config
import kotlinx.android.synthetic.main.fragment_sanisette_detail.*

class SanisetteActionsHelper(context: Context) {

    enum class SortOptions {
        BY_NAME, BY_DISTANCE, BY_BOROUGH
    }

    private var currentLocation: Location? = null
    private var showOpened = true
    private var showClosed = true
    private var showFavorites = true
    private var currentSort = BY_NAME

    val prefs = context.getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE)
    val favorites = prefs.getStringSet(Config.KEY_FAVORITES, HashSet())

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
            (showOpened && it.opened) || (showClosed && !it.opened) || (showFavorites && (favorites.contains(it.objectId.toString())))
        }.sortedWith(
                when(currentSort) {
                    BY_NAME -> compareBy ({ it.streetName }, {it.streetNumber})
                    BY_DISTANCE -> compareBy ({it.distance}, { it.streetName }, {it.streetNumber})
                    BY_BOROUGH -> compareBy ({it.borough}, { it.streetName }, {it.streetNumber})
                }))

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

    fun sortBy(sortOptions: SortOptions){
        currentSort = sortOptions
        sanisettesVariable.publish()
    }

    fun addOrRemoveFavorite(info: SanisetteInfo){
        if (favorites.contains(info.objectId.toString())){
            favorites.remove(info.objectId.toString())
        } else {
            favorites.add(info.objectId.toString())
        }
        prefs.edit().putStringSet(Config.KEY_FAVORITES, favorites).apply()
    }
}