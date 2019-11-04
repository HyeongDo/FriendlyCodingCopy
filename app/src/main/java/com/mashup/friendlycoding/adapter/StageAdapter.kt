package com.mashup.friendlycoding.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.model.StageItem
import kotlinx.android.synthetic.main.item_stage.view.*

class StageAdapter(
    val context: Context,
    val items: ArrayList<StageItem>
) : RecyclerView.Adapter<StageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_stage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setOnClickListener {
            if (position == 0) {
                Log.d("TAG", "${position}번째")
            } else if (position == 1) {
                Log.d("TAG", "${position}번째")
            } else if (position == 2) {
                Log.d("TAG", "${position}번째")
            } else if (position == 3) {
                Log.d("TAG", "${position}번째")
            } else if (position == 4) {
                Log.d("TAG", "${position}번째")
            }
        }

        var check = position % 5
        if (check == 2) {
            //holder.bind(items[position], context)
            holder.apply {
                bind(items[position], context)
                itemView.tag = items

                itemView.stageImage.setPadding(650, 0, 0, 0)
            }
        } else if (check == 1 || check == 3) {
            holder.apply {
                bind(items[position], context)
                itemView.tag = items
                itemView.stageImage.setPadding(400, 0, 0, 0)
            }
        } else if (check == 0 || check == 4) {
            holder.apply {
                bind(items[position], context)
                itemView.tag = items
                itemView.stageImage.setPadding(50, 0, 0, 0)
            }
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.stageImage)
        fun bind(list: StageItem, context: Context) {
            image?.setImageResource(R.drawable.cave)
        }
    }
}