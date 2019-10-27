package com.mashup.friendlycoding.viewmodel

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mashup.friendlycoding.PlayActivity
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.model.Run
import com.mashup.friendlycoding.repository.CodeBlock

class CodeBlockViewModel : ViewModel() {
    val monsterImage: Int = R.drawable.monster
    val princessImage: Int = R.drawable.princess
    val gridImage: Int = R.drawable.grid

    private var mCodeBlock =  MutableLiveData<ArrayList<CodeBlock>>()
    //private var mRepo = CodeBlocksRepository()
    private var mRun = Run()
    private var mIsUpdating = MutableLiveData<Boolean>(false)
    private val blockButton = arrayListOf(CodeBlock("move();"),
        CodeBlock("turnLeft();"),
        CodeBlock("turnRight();"),
        CodeBlock("pickAxe();"))

    fun getRunModel() : Run {
        return mRun
    }

    fun getBlockButton () : ArrayList<CodeBlock> {
        return blockButton
    }

    fun init() {
        val codeBlock = ArrayList<CodeBlock>()
        mCodeBlock.value = codeBlock
    }

    fun getCodeBlock () : LiveData<ArrayList<CodeBlock>> {
        return mCodeBlock
    }

    fun addNewBlock(codeBlock : CodeBlock) {
        mIsUpdating.value = true
        Log.e("${codeBlock.funcName} ", "")
        val block : ArrayList<CodeBlock> = mCodeBlock.value!!
        mCodeBlock.value!!.add(codeBlock)
        mCodeBlock.postValue(block)
        mIsUpdating.postValue(false)
    }

    //TODO : 최종 CodeBlock 들을 실제 실행하는 함수
    fun run (view : View) {
        Log.e("Run", "실행")
        mRun.setCodeBlock(mCodeBlock.value!!)
        mRun.run()
    }
}