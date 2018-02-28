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
import com.cryocrystal.waytocludgie.model.SanisettesResponse
import com.cryocrystal.waytocludgie.presenter.MainPresenter
import com.cryocrystal.waytocludgie.presenter.SanisettListContract
import kotlinx.android.synthetic.main.fragment_sanisettes_list.*

class SanisettesListFragment : PresenterFragment<MainPresenter>(), SanisettListContract {

    lateinit private var adapter: SanisettesAdapter

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
        return inflater.inflate(R.layout.fragment_sanisettes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SanisettesAdapter(onItemClicked)
        rvSanisettes.layoutManager = LinearLayoutManager(context)
        rvSanisettes.adapter = adapter
    }

    private val onItemClicked = View.OnClickListener { view ->

    }

    override fun createPresenter(): MainPresenter {
        return (activity as MainActivity).presenter
    }
}