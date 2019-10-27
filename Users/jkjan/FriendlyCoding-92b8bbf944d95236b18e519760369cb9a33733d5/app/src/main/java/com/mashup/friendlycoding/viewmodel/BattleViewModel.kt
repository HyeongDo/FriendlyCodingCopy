package com.mashup.friendlycoding.viewmodel

import android.app.Application
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.adapter.HP
import com.mashup.friendlycoding.repository.BattleRepository
import com.mashup.friendlycoding.repository.CodeBlock
import kotlin.collections.ArrayList
import android.widget.FrameLayout


class BattleViewModel() {
    private var mCodeBlock: MutableLiveData<ArrayList<CodeBlock>>? = null
    private var monsterHP : MutableLiveData<ArrayList<HP>>? = null
    private var mRepo = BattleRepository()

    fun init() {
        if (mCodeBlock != null) {
            return
        }

        mRepo = BattleRepository().getInstance()
        mCodeBlock = mRepo.getCodeBlock()
        monsterHP = mRepo.getMonsterMaxHP()
    }

    fun getCodeBlock () : LiveData<ArrayList<CodeBlock>>? {
        return mCodeBlock
    }

    fun getHP() : LiveData<ArrayList<HP>>? {
        return monsterHP
    }

    fun monsterAttack (attackType : Int) {
        if (attackType == 0) { // FIRE
            //TODO : 파이어볼이 나가는 이미지를 생성 ... Layout과 Context를 View로부터 받아야 하는데, 어떻게?
        }

        else if (attackType == 1) { // EARTH
        }
    }

    fun monsterAttacked () {
        val currentHP : ArrayList<HP> = monsterHP!!.value!!
        monsterHP!!.value!!.removeAt(currentHP.size-1)
        monsterHP!!.postValue(currentHP)
    }
}