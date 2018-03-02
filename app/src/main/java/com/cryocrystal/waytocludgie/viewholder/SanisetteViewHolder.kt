package com.cryocrystal.waytocludgie.viewholder

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.cryocrystal.mvp.GenericViewHolder
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.statics.Tools
import kotlinx.android.synthetic.main.item_sanisette.view.*

class SanisetteViewHolder(parent: ViewGroup, onClickListener: View.OnClickListener) : GenericViewHolder<SanisetteInfo>(createView(parent, R.layout.item_sanisette)) {

    private val openingDrawable: Drawable? by lazy {
        ContextCompat.getDrawable(context, R.drawable.toilet_opening)
    }
    private val closingDrawable: Drawable? by lazy {
        ContextCompat.getDrawable(context, R.drawable.toilet_closing)
    }

    init {
        itemView.setOnClickListener(onClickListener)
    }

    override fun bind(item: SanisetteInfo?) {
        super.bind(item)
        if (item == null) {
            return
        }

        itemView.tvStreetNumber.text = item.streetNumber
        itemView.tvStreetName.text = item.streetName
        itemView.tvBorough.text = context.getString(R.string.borough_formated, item.borough)

        val distance = item.distance
        itemView.tvDistance.visibility = if (distance != null) View.VISIBLE else View.GONE
        if (distance != null){
            itemView.tvDistance.text = Tools.formatDistance(context, distance)
        }


        itemView.tvOpened.text = context.getString(if (item.opened) R.string.sanisette_opened else R.string.sanisette_closed)
        itemView.tvOpened.setTextColor(ContextCompat.getColor(context, if (item.opened) R.color.sanisette_opened else R.color.sanisette_closed))

        val anim = (if (item.opened) openingDrawable else closingDrawable)?.mutate()
        if (anim != null){
            itemView.ivStatus.setImageDrawable(anim)
            (anim as AnimationDrawable).start()
        }

        itemView.tag = item
    }
}