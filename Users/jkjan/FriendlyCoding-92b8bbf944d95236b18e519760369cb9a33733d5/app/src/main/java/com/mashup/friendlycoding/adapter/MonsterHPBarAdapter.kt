package com.mashup.friendlycoding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.repository.CodeBlock

class HP

class MonsterHPBarAdapter(val mContext: Context, val HPBar: ArrayList<HP>) :
    RecyclerView.Adapter<MonsterHPBarAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.monster_hp_bar, viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // Set the name of the 'CodeBlock'
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun getItemCount(): Int {
        return HPBar.size
    }
}