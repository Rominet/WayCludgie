package com.cryocrystal.waytocludgie.fragment

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.activity.MainActivity
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.statics.Tools
import kotlinx.android.synthetic.main.fragment_sanisette_detail.*
import java.util.*


class SanisetteDetailFragment : Fragment() {

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

        val distance = info.distance
        tvDistance.visibility = if (distance != null) View.VISIBLE else View.GONE
        if (distance != null) {
            tvDistance.text = Tools.formatDistance(context!!, distance)
        }

        tvOpeningHours.text = info.getOpeningHours(context!!)

        tvOpened.text = context!!.getString(if (info.opened) R.string.sanisette_opened else R.string.sanisette_closed)
        tvOpened.setTextColor(ContextCompat.getColor(context!!, if (info.opened) R.color.sanisette_opened else R.color.sanisette_closed))

        val anim = ContextCompat.getDrawable(context!!, if (info.opened) R.drawable.toilet_opening else R.drawable.toilet_closing)
        if (anim != null) {
            ivStatus.setImageDrawable(anim)
            // Delay animation to the end of transition
            ivStatus.postDelayed({ (anim as AnimationDrawable).start() }, resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
        }

        btDirections.setOnClickListener { onDirectionClicked(info) }
        btShare.setOnClickListener { onSharedClicked(info) }
        initFavorite(info)

        view.setOnClickListener {
            close()
        }

        //Show action bar at the end of transition (smooth ui)
        view.postDelayed({ (activity as MainActivity).displayActionBar(true) }, resources.getInteger(android.R.integer.config_longAnimTime) + 100L)
    }

    private fun initFavorite(info: SanisetteInfo) {
        val actionsHelper = (activity as MainActivity).presenter.actionsHelper
        btFavorite.isChecked = actionsHelper.favorites.contains(info.objectId.toString())

        btFavorite.setOnClickListener {
            actionsHelper.addOrRemoveFavorite(info)
        }
    }

    private fun onDirectionClicked(info: SanisetteInfo) {
        val gmmIntentUri = Uri.parse("google.navigation:q=${info.lat},${info.lng}(Sanisette)&mode=w")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.`package` = "com.google.android.apps.maps"
        if (mapIntent.resolveActivity(activity?.packageManager) != null) { // If the user has googleMap
            startActivity(mapIntent);
        } else {
            val webMapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlFromInfo(info)))
            startActivity(webMapIntent)
        }
    }

    private fun urlFromInfo(info: SanisetteInfo): String =
            "https://www.google.com/maps/dir/?api=1&destination=${info.lat},${info.lng}&destination_place_id=Sanisette&travelmode=walking"

    private fun onSharedClicked(info: SanisetteInfo) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        val funnyAnswers = context!!.resources.getStringArray(R.array.going_to_the_wc)
        val msg = getString(R.string.share_message, funnyAnswers[Random().nextInt(funnyAnswers.size)], urlFromInfo(info))
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.send_to)))
    }

    fun close() {
        fragmentManager?.popBackStack()
        onClose()
    }

    private fun onClose() {
        (activity as MainActivity).displayActionBar(false)
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