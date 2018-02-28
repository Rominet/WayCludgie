package com.cryocrystal.waytocludgie.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryocrystal.mvp.app.PresenterFragment
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.activity.MainActivity
import com.cryocrystal.waytocludgie.adapter.rv.SanisettesAdapter
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.model.SanisetteRecordItem
import com.cryocrystal.waytocludgie.model.SanisettesResponse
import com.cryocrystal.waytocludgie.presenter.MainPresenter
import com.cryocrystal.waytocludgie.presenter.SanisettListContract
import com.cryocrystal.waytocludgie.presenter.SanisettesListPresenter
import kotlinx.android.synthetic.main.fragment_sanisettes_list.*

class SanisettesListFragment : PresenterFragment<SanisettesListPresenter>(), SanisettListContract {

    lateinit private var adapter: SanisettesAdapter

    override fun onDisplayLoader() {

    }

    override fun onSanisettesUpdated(sanisettes: List<SanisetteInfo>?) {
        adapter.setSanisettes(sanisettes)
    }

    override fun onWebError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sanisettes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("View created !! : " + context)

        adapter = SanisettesAdapter(onItemClicked)
        rvSanisettes.layoutManager = LinearLayoutManager(context)
        rvSanisettes.adapter = adapter
    }

    private val onItemClicked = View.OnClickListener { view ->

    }

    override fun createPresenter(): SanisettesListPresenter {
        return SanisettesListPresenter((activity as MainActivity).presenter, this)
    }
}