package com.mashup.friendlycoding.adapter

import android.content.Context
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.databinding.ItemCodeBlockListBinding
import com.mashup.friendlycoding.viewmodel.CodeBlockViewModel
import kotlinx.android.synthetic.main.item_code_block_list.view.*
import android.text.Editable
import com.mashup.friendlycoding.ignoreBlanks
import com.mashup.friendlycoding.model.CodeBlock
import java.lang.Exception

class CodeBlockAdapter(
    private val mContext: Context,
    private val CodeBlocks: ArrayList<CodeBlock>,
    private val mCodeBlockViewModel: CodeBlockViewModel
) :
    RecyclerView.Adapter<CodeBlockAdapter.Holder>() {
    var clickable = true
    lateinit var binding: ItemCodeBlockListBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.item_code_block_list, viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // Set the name of the 'CodeBlock'
        //binding.position = position
        //holder.bind(CodeBlocks[position], mContext)
        //생성된 View에 보여줄 데이터를 설정
        val item = CodeBlocks[position]

        //길게 눌렀을 때
        val listener = View.OnLongClickListener {
            if (clickable) {
                Toast.makeText(it.context, "${item.funcName}가 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                //CodeBlocks.removeAt(position)
                mCodeBlockViewModel.deleteBlock(position)

                if (item.type == 1 || item.type == 2) {
                    mCodeBlockViewModel.changeBlockLevel(false)
                }
                else if (item.type == 4) {
                    mCodeBlockViewModel.changeBlockLevel(true)
                }
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, CodeBlocks.size)
            }
            true
        }

        val type2BlockListener = View.OnClickListener {
            if (clickable && item.type == 2) {
                mCodeBlockViewModel.mRun.insertBlockPosition = position
                Toast.makeText(
                    it.context,
                    "${item.funcName}이 선택되었습니다. 조건을 추가해주세요. ${mCodeBlockViewModel.mRun.insertBlockPosition}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mCodeBlockViewModel.mRun.insertBlockAt.postValue(-1)
                Log.e("해제", "${mCodeBlockViewModel.mRun.insertBlockPosition}")
            }
        }

        holder.apply {
            bind(listener, type2BlockListener, item)
            itemView.tag = item
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView

        fun bind(listener: View.OnLongClickListener, type2BlockListener: View.OnClickListener, codeBlock: CodeBlock) {
            view.func_name.text = codeBlock.funcName

            if (codeBlock.type == 2)
                view.end.text = "{"
            else {
                view.end.text = ""
            }

            if (codeBlock.type == 1) {
                view.argument.text.clear()
                view.argument.isVisible = true
                view.argument.isCursorVisible = false
                view.argument.isClickable = true
//                if ( view.argument.text != null)
//                    view.argument.hint =  view.argument.text
//                else
//                    view.argument.hint="?"
                view.end.text = ") {"

                view.argument.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        try {
                            codeBlock.argument = s.toString().toInt()
                        }
                        catch (e: Exception) { }
                    }

                    override fun afterTextChanged(arg0: Editable) {}
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    }
                })
            }

            else {
                view.argument.isVisible = false
                view.argument.isClickable = false
            }

            view.setOnLongClickListener(listener)
            view.setOnClickListener(type2BlockListener)
        }
    }

    override fun getItemCount(): Int {
        return CodeBlocks.size
    }
}