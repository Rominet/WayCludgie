package com.cryocrystal.waytocludgie.presenter

import com.cryocrystal.mvp.presenter.Presenter
import com.cryocrystal.waytocludgie.contractbehaviour.SanisettesCallback

class SanisettesListPresenter(private val mainPresenter: MainPresenter, private val contract: SanisettListContract) : Presenter(){

    override fun create() {
        super.create()

        link(mainPresenter.sanisettesVariable.observable.subscribe {
            contract.onSanisettesUpdated(it.toList())
        })
    }
}

interface SanisettListContract : SanisettesCallback