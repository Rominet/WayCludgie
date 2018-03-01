package com.cryocrystal.waytocludgie.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import kotlinx.android.synthetic.main.fragment_sanisette_detail.*

class SanisetteDetailFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sanisette_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments ?: return

        val info = args.get(KEY_INFO) as SanisetteInfo;
        tvStreetName.text = info.streetName
        tvStreetNumber.text = info.streetNumber
        tvBorough.text = getString(R.string.borough_formated, info.borough)
        tvAdministrator.text = info.administrator
        tvSource.text = info.source

        tvOpeningHours.text = info.getOpeningHours(context!!)
        view.setOnClickListener {
            activity?.fragmentManager?.popBackStack()
        }
    }

    companion object {

        const val KEY_INFO = "KeyInfo"
        const val TAG = "SanisetteDetailFragment"

        fun newInstance(info: SanisetteInfo): SanisetteDetailFragment {
            val args = Bundle()
            args.putParcelable(KEY_INFO, info)

            val fragment = SanisetteDetailFragment()
            fragment.arguments = args

            return fragment
        }
    }
}