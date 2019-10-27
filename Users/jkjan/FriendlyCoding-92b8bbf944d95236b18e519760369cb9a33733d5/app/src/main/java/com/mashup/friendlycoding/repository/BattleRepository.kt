package com.mashup.friendlycoding.repository

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.mashup.friendlycoding.adapter.HP

class CodeBlock (val funcName : String)

class BattleRepository {
    private var instance : BattleRepository? = null

    fun getInstance() : BattleRepository {
        if (instance == null) {
            instance = BattleRepository()
        }
        return instance!!
    }

    //pretend to get data from a webservice or online source
    fun getCodeBlock() : MutableLiveData<ArrayList<CodeBlock>> {
        val data = MutableLiveData<ArrayList<CodeBlock>>()

        //mock data
        data.value = arrayListOf(
            //이것은 mock 이므로 앞에 탭 당 4칸짜리 공백을 손수 쓴 것이나, 실제 코드 블록을 쌓을 땐
            //다른 코드 에디터들이 하는 것처럼 자동으로 앞에 빈 view를 만들어줘야 할 것.
            CodeBlock("killBoss(spell) {"),
            CodeBlock("    while(bossHP > 10) {"),
            CodeBlock("        if ((attackType = attackDetect()) != -1) {"),
            CodeBlock("            if (attackType == FIRE) {"),
            CodeBlock("                moveFront();"),
            CodeBlock("                wait();"),
            CodeBlock("                moveBack();"),
            CodeBlock("                moveBack();"),
            CodeBlock("                moveFront();"),
            CodeBlock("            }"),
            CodeBlock("            if (attackType == EARTH) {"),
            CodeBlock("                for(3)"),
            CodeBlock("                    jump();"),
            CodeBlock("            }"),
            CodeBlock("        }"),
            CodeBlock("            "),
            CodeBlock("        else"),
            CodeBlock("            attack();"),
            CodeBlock("    }"),
            CodeBlock("    castSpell(spell);"),
            CodeBlock("}")
            )
        return data
    }

    fun getMonsterMaxHP () : MutableLiveData<ArrayList<HP>> {
        val data = MutableLiveData<ArrayList<HP>>()
        data.value = arrayListOf(HP(), HP(), HP(), HP(), HP(), HP(), HP(), HP(), HP(), HP()) // 람다는 어려워
        return data
    }
}