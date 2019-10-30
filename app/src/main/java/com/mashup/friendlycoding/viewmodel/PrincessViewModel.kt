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

    val mapList = arrayOf(
        arrayOf(0, 0, 0, 0, 1, 0, 1, 0, 0, 0),
        arrayOf(1, 1, 0, 0, 1, 0, 1, 1, 1, 1),
        arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 0, 0),
        arrayOf(1, 0, 1, 1, 1, 0, 1, 1, 1, 1),
        arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(1, 0, 0, 0, 0, 1, 1, 1, 1, 0),
        arrayOf(1, 1, 1, 1, 0, 1, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 1, 0, 1, 0, 0),
        arrayOf(0, 1, 1, 1, 1, 1, 0, 1, 1, 1),
        arrayOf(0, 0, 0, 0, 0, 1, 0, 0, 0, 0)
    )

    var nowX = 0
    var nowY = 9

    private var princessImg: ImageView? = null
    private var win: TextView? = null
    var direction = 1
    var oneBlock = 0f
    var unit = 1
    var width = 0
    var sale = true
    val n = 10
    var isLost = MutableLiveData<Boolean>()

    fun move(i: Int) {
        when (i) {
            -1 -> clear()
            0 -> go()
            1 -> rotationLeft()
            2 -> rotationRight()
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
        isLost.value = false
        clear()
    }

    fun go() {
        changeXY()
        check()
    }

    fun check() {
        Log.e("(nowX", "(nowX  $nowX,,,$nowY")
        if (nowX < 10 && nowX > -1 && nowY < 10 && nowY > -1) {
            if (mapList[nowY][nowX] == 1) {
                isLost.value = true
            } else if (nowY == 9 && nowX == 9) {
                win!!.visibility = VISIBLE
            }
        } else {
            isLost.value = false
        }
    }

    fun rotationLeft() {
        direction -= 1
        if (direction < 0)
            direction += 4
    }

    fun rotationRight() {
        if (direction == 3)
            direction = 0
        else
            direction++
    }

    fun changeXY() {
        val one = oneBlock
        direction %= 4
        when (direction) {
            //goint up
            0 -> {
                princessImg!!.y = (princessImg!!.y - one)
                nowY--
            }

            //going right
            1 -> {
                princessImg!!.x = (princessImg!!.x + one)
                nowX++
            }

            //going down
            2 -> {
                princessImg!!.y = (princessImg!!.y + one)
                nowY++
            }

            //going left
            3 -> {
                princessImg!!.x = (princessImg!!.x - one)
                nowX--
            }
        }
    }

    fun clear() {
        unit = 1
        nowX = 0
        nowY = 9
        direction = 1
        princessImg!!.x = oneBlock * nowX - oneBlock * 0.1f
        princessImg!!.y = oneBlock * nowY - oneBlock * 0.23f
    }

    fun runBoss() {
        metBoss.value = !metBoss.value!!
        //Thread.sleep(1000)
        //metBoss.postValue(false)
    }
}