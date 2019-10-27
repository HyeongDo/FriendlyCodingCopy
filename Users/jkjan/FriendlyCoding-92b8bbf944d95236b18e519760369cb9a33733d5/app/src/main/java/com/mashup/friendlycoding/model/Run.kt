package com.mashup.friendlycoding.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mashup.friendlycoding.repository.CodeBlock

class Run {
    private var moveView = MutableLiveData<Int>()
    private var mCodeBlock : ArrayList<CodeBlock>? = null

    fun setCodeBlock (mCodeBlock: ArrayList<CodeBlock>) {
        this.mCodeBlock = mCodeBlock
    }

    fun getMoving() : LiveData<Int> {
        return moveView
    }

    fun run() {
        for (i in 0 until  mCodeBlock!!.size) {
            if (mCodeBlock!![i].funcName == "move();") {
                moveView.value = 0
                Log.e("갑니다", "가요")
            }

            if (mCodeBlock!![i].funcName == "turnLeft();") {
                moveView.value = 1
                Log.e("돕니다", "돌아요")
            }
        }
    }
}