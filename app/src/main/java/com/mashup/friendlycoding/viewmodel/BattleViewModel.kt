package com.mashup.friendlycoding.viewmodel

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.mashup.friendlycoding.model.RunModel

class BattleViewModel(private val hpBar : View, var princessImg : View, var monsterImg : View, var princessAttackImg : View) {
    var mRun = RunModel()
    private lateinit var param : ConstraintLayout.LayoutParams

    fun monsterAttacked() {
        param.marginEnd += 100
        hpBar.layoutParams = param
        princessAttackImg.isVisible = true
    }

    fun init() {
        param = hpBar.layoutParams as ConstraintLayout.LayoutParams
    }
}