package com.cryocrystal.waytocludgie.presenter

import com.cryocrystal.mvp.presenter.Presenter
import com.cryocrystal.waytocludgie.actions.SanisetteActionsHelper
import com.cryocrystal.waytocludgie.contractbehaviour.SanisettesCallback

class SanisettesListPresenter(val actionsHelper: SanisetteActionsHelper, private val contract: SanisettListContract) : Presenter(){

    override fun create() {
        super.create()

        link(actionsHelper.sanisettesObservable.subscribe {
            actionsHelper.treatResults(it, contract)
        })
    }
}

interface SanisettListContract : SanisettesCallback