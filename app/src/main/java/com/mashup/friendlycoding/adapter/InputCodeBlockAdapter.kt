package com.mashup.friendlycoding.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.repository.CodeBlock
import com.mashup.friendlycoding.viewmodel.CodeBlockViewModel
import kotlinx.android.synthetic.main.input_code_list.view.*

class InputCodeBlockAdapter(
    val mCodeBlockViewModel: CodeBlockViewModel,
    val adapter: CodeBlockAdapter
) :
    RecyclerView.Adapter<InputCodeBlockAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        //보여줄 아이템 개수만큼 View를 생성
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.input_code_list, parent, false)
        return ViewHolder(inflatedView)

    }

    override fun getItemCount() = mCodeBlockViewModel.getBlockButton().size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mCodeBlockViewModel.getBlockButton()[position]

        val listener = View.OnClickListener {
            mCodeBlockViewModel.addNewBlock(mCodeBlockViewModel.getBlockButton()[position])
            adapter.notifyDataSetChanged()
            Log.e("click", "Clicked")

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