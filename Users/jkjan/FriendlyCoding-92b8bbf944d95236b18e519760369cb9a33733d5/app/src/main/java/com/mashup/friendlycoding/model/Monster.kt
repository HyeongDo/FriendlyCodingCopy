package com.mashup.friendlycoding.model

import android.app.Application
import com.mashup.friendlycoding.viewmodel.BattleViewModel
import kotlin.random.Random

class Monster (val maxHP : Int) {
    var currentHP = maxHP

    // java와 kotlin엔 왜 #define 이 없는지??????
    private val FIRE = 0
    private val EARTH = 1

    private var attackType : Int = 0
    private var mBattleViewModel = BattleViewModel()

    fun attacked(DPS : Int) {
        currentHP -= DPS
        mBattleViewModel.monsterAttacked()
    }

    fun attack(princess: Princess) {
        attackType = Random.nextInt(0, 2)

        if (attackType == FIRE) {
            mBattleViewModel.monsterAttack(FIRE)

        }

        else if (attackType == EARTH) {
            mBattleViewModel.monsterAttack(EARTH)
        }
    }
}