package com.cryocrystal.waytocludgie.adapter.rv

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.model.SanisetteRecordItem
import com.cryocrystal.waytocludgie.viewholder.SanisetteViewHolder

class SanisettesAdapter(private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sanisettes: MutableList<SanisetteInfo> = ArrayList<SanisetteInfo>()

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
        return sanisettes[position].objectId.toLong()
    }

    override fun getItemCount(): Int {
        return sanisettes.size
    }

    fun setSanisettes(sanisettes: List<SanisetteInfo>?){
        this.sanisettes.clear()
        if (sanisettes != null){
            this.sanisettes.addAll(sanisettes)
        }
        notifyDataSetChanged()
    }
}