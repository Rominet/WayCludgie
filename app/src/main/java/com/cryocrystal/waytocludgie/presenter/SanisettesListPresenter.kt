package com.cryocrystal.waytocludgie.presenter

import com.cryocrystal.mvp.presenter.Presenter
import com.cryocrystal.waytocludgie.contractbehaviour.DataLoading
import com.cryocrystal.waytocludgie.model.SanisettesResponse

class SanisettesListPresenter(private val contract: SanisettListContract) : Presenter(){}

interface SanisettListContract : DataLoading<SanisettesResponse>