package com.cryocrystal.waytocludgie.api

import com.cryocrystal.waytocludgie.model.SanisettesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface SanisettesApi {
    @GET("search")
    fun getCompleteList(@Query("dataset")dataset: String = "sanisettesparis2011", @Query("rows")rows : Int = 1000) : Observable<SanisettesResponse>
}