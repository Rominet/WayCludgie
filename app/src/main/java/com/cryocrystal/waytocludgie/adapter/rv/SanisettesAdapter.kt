package com.cryocrystal.waytocludgie.adapter.rv

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.cryocrystal.mvp.GenericViewHolder
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.model.SanisetteRecordItem

class SanisettesAdapter(private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sanisettes: MutableList<SanisetteRecordItem> = ArrayList<SanisetteRecordItem>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SanisetteViewHolder(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as SanisetteViewHolder).bind(sanisettes[position])
    }

    override fun getItemId(position: Int): Long {
        // There are so many useless fields in their DB, I'm not sure this one is the relevant one.
        return sanisettes[position].fields.objectid.toLong()
    }

    override fun getItemCount(): Int {
        return sanisettes.size
    }

    fun setSanisettes(sanisettes: List<SanisetteRecordItem>){
        this.sanisettes.clear()
        this.sanisettes.addAll(sanisettes)
        notifyDataSetChanged()
    }
}

class SanisetteViewHolder(parent: ViewGroup, onClickListener: View.OnClickListener) : GenericViewHolder<SanisetteRecordItem>(GenericViewHolder.createView(parent, R.layout.item_sanisette)) {

    override fun bind(item: SanisetteRecordItem?) {
        super.bind(item)
        if (item == null){
            return
        }


    }
}
