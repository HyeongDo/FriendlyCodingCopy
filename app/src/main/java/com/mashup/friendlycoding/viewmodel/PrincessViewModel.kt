package com.mashup.friendlycoding.viewmodel

import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrincessViewModel : ViewModel() {
    var metBoss = MutableLiveData<Boolean>()
    var princessImg: ImageView? = null
    private var win: TextView? = null
    private var oneBlock = 0f
    private val n = 10
    var width = 0

    fun move(i: Int) {
        when (i) {
            -1 -> clear()
            0 -> go(0)  // up
            1 -> go(1)  // right
            2 -> go(2)  // down
            3 -> go(3)  // left
            4 -> rotationLeft()
            5 -> rotationRight()
        }
    }

    fun setPrincessImage(view: ImageView, win: TextView) {
        this.princessImg = view
        this.win = win
        metBoss.value = false
    }

    fun setViewSize(width: Int) {
        this.width = width
        oneBlock = (width / n + width % n).toFloat()
        //this.princessImg?.height ?: oneBlock.toInt()
        clear()
    }

    private fun rotationLeft() {
        // TODO : 공주 사진 변경
    }

    private fun rotationRight() {
        // TODO : 공주 사진 변경
    }

    private fun go(direction: Int) {
        when (direction) {
            //goint up
            0 -> {
                princessImg!!.y = (princessImg!!.y - oneBlock)
            }
            //going right
            1 -> {
                princessImg!!.x = (princessImg!!.x + oneBlock)
            }

            //going down
            2 -> {
                princessImg!!.y = (princessImg!!.y + oneBlock)
            }

            //going left
            3 -> {
                princessImg!!.x = (princessImg!!.x - oneBlock)
            }
        }
    }

    fun clear() {
        princessImg!!.x = oneBlock * 0 - oneBlock * 0.1f
        princessImg!!.y = oneBlock * 9 - oneBlock * 0.23f
    }
}