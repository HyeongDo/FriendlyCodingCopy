package com.mashup.friendlycoding.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.viewmodel.CodeBlock
import com.mashup.friendlycoding.viewmodel.CodeBlockViewModel
import com.mashup.friendlycoding.viewmodel.MapSettingViewModel
import kotlinx.android.synthetic.main.item_input_code_list.view.*

class InputCodeBlockAdapter(private val mCodeBlockViewModel: CodeBlockViewModel, var inputCodeBlock : ArrayList<CodeBlock>) : RecyclerView.Adapter<InputCodeBlockAdapter.ViewHolder>() {
        var clickable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        //보여줄 아이템 개수만큼 View를 생성
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_input_code_list, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount() = inputCodeBlock.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = inputCodeBlock[position]
        val listener = View.OnClickListener {
            if (clickable) {
                mCodeBlockViewModel.addNewBlock(item)
            }
        }

        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        //ViewHolder 단위 객체로 View의 데이터를 설정
        private var view: View = v

        fun bind(listener: View.OnClickListener, item: CodeBlock) {
            view.input_code_name.text = item.funcName
            view.setOnClickListener(listener)
        }
    }
}