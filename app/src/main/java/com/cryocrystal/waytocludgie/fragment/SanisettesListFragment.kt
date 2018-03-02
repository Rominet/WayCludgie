package com.cryocrystal.waytocludgie.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.cryocrystal.mvp.app.PresenterFragment
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.actions.SanisetteActionsHelper
import com.cryocrystal.waytocludgie.activity.MainActivity
import com.cryocrystal.waytocludgie.adapter.rv.SanisettesAdapter
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.presenter.SanisettListContract
import com.cryocrystal.waytocludgie.presenter.SanisettesListPresenter
import kotlinx.android.synthetic.main.fragment_sanisettes_list.*


class SanisettesListFragment : PresenterFragment<SanisettesListPresenter>(), SanisettListContract {

    lateinit private var adapter: SanisettesAdapter

    override fun onSanisettesUpdated(sanisettes: List<SanisetteInfo>?) {
        adapter.setSanisettes(sanisettes)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sanisettes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SanisettesAdapter(onItemClicked)
        rvSanisettes.layoutManager = LinearLayoutManager(context)
        rvSanisettes.adapter = adapter

        tvFiltersExpander.setOnClickListener {
            val expanded = llFiltersExpanded.visibility == View.VISIBLE
            llFiltersExpanded.visibility = if (expanded) View.GONE else View.VISIBLE
            animateArrow(expanded)
        }

        cbShowOpened.setOnCheckedChangeListener{ _, isChecked ->
            presenter.actionsHelper.filterOpened(isChecked)
        }
        cbShowClosed.setOnCheckedChangeListener{ _, isChecked ->
            presenter.actionsHelper.filterClosed(isChecked)
        }
        cbShowFavorites.setOnCheckedChangeListener{ _, isChecked ->
            presenter.actionsHelper.filterFavorites(isChecked)
        }

        spSorter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                presenter.actionsHelper.sortBy(SanisetteActionsHelper.SortOptions.values()[position])
            }

        }
        ibRefresh.setOnClickListener {
            (activity as MainActivity).presenter.forceRefresh()
        }
    }

    private fun animateArrow(expanded: Boolean){
        ivFiltersArrow.animate().rotation(if (expanded) 0f else 180f).start()
    }

    private val onItemClicked = View.OnClickListener { view ->
        (activity as MainActivity).displayDetail(view.tag as SanisetteInfo)
    }

    override fun createPresenter(): SanisettesListPresenter {
        return SanisettesListPresenter((activity as MainActivity).presenter.actionsHelper, this)
    }
}