package com.cryocrystal.waytocludgie.viewholder

import android.view.View
import android.view.ViewGroup
import com.cryocrystal.mvp.GenericViewHolder
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.statics.Tools
import kotlinx.android.synthetic.main.item_sanisette.view.*

class SanisetteViewHolder(parent: ViewGroup, onClickListener: View.OnClickListener) : GenericViewHolder<SanisetteInfo>(createView(parent, R.layout.item_sanisette)) {

    init {
        itemView.setOnClickListener(onClickListener)
    }

    override fun bind(item: SanisetteInfo?) {
        super.bind(item)
        if (item == null){
            return
        }

        itemView.tvStreetNumber.text = item.streetNumber
        itemView.tvStreetName.text = item.streetName
        itemView.tvBorough.text = context.getString(R.string.borough_formated, item.borough)
        itemView.tvDistance.visibility = if (item.distance > 0) View.VISIBLE else View.GONE
        itemView.tvDistance.text = Tools.formatDistance(context, item.distance)

        itemView.tag = item
    }
}