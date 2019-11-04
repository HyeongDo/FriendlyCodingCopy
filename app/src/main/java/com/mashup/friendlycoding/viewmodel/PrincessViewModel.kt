package com.mashup.friendlycoding.viewmodel

import android.util.Log
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mashup.friendlycoding.R
import kotlinx.android.synthetic.main.activity_main.*

class PrincessViewModel : ViewModel() {
    var metBoss = MutableLiveData<Boolean>()

    private var princessImg: ImageView? = null
    private var win: TextView? = null
    var oneBlock = 0f
    var width = 0
    var sale = true
    val n = 10

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
        this.princessImg?.height ?: oneBlock.toInt()
        clear()
    }

    //fun go(direction: Int) {
        //changeXY(direction)
        //check()
    //}
//
//    fun check() {
//        if (nowX < 10 && nowX > -1 && nowY < 10 && nowY > -1) {
//            if (mapList[nowY][nowX] == 1) {
//                isLost.value = true
//            } else if (nowY == 9 && nowX == 9) {
//                win!!.visibility = VISIBLE
//            }
//        } else {
//            isLost.value = false
//        }
//    }
//
//    fun sendSignal(WinOrLose : Boolean) {
//        isLost.value = WinOrLose
//    }

    fun rotationLeft() {
        // TODO : 공주 사진 변경
//        direction -= 1
//        if (direction < 0)
//            direction += 4
    }

    fun rotationRight() {
        // TODO : 공주 사진 변경
//        if (direction == 3)
//            direction = 0
//        else
//            direction++
    }

    fun go(direction : Int) {
        val one = oneBlock
        //direction %= 4
        when (direction) {
            //goint up
            0 -> {
                princessImg!!.y = (princessImg!!.y - one)
                //nowY--
            }

            //going right
            1 -> {
                princessImg!!.x = (princessImg!!.x + one)
                //nowX++
            }

            //going down
            2 -> {
                princessImg!!.y = (princessImg!!.y + one)
                //nowY++
            }

            //going left
            3 -> {
                princessImg!!.x = (princessImg!!.x - one)
                //nowX--
            }
        }
    }

    fun clear() {
        princessImg!!.x = oneBlock * 0 - oneBlock * 0.1f
        princessImg!!.y = oneBlock * 9 - oneBlock * 0.23f
    }

    fun runBoss() {
        metBoss.value = !metBoss.value!!
    }
}