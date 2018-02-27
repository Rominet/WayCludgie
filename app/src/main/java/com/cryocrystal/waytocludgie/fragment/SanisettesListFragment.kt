package com.cryocrystal.waytocludgie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryocrystal.mvp.app.PresenterFragment
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.model.SanisettesResponse
import com.cryocrystal.waytocludgie.presenter.SanisettListContract
import com.cryocrystal.waytocludgie.presenter.SanisettesListPresenter

class SanisettesListFragment : PresenterFragment<SanisettesListPresenter>(), SanisettListContract {

    override fun onDisplayLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccess(data: SanisettesResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWebError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sanisettes_list, container, false)
        return view
    }

    override fun createPresenter(): SanisettesListPresenter {
        return SanisettesListPresenter(this)
    }
}