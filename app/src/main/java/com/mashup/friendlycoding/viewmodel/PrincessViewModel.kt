package com.mashup.friendlycoding.viewmodel

import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.Context
import android.view.animation.AnimationUtils
import android.graphics.Matrix




class PrincessViewModel : ViewModel() {
    var metBoss = MutableLiveData<Boolean>()

    private var princessImg: ImageView? = null
    private var win: TextView? = null
    private var oneBlock = 0f
    private val n = 10
    private var princessContext: Context? = null
    var width = 0

    private var direction = 0

    val matrix = Matrix()
    fun move(i: Int) {
        when (i) {
            -1 -> clear()
            0 -> {
                go(0)
                direction = 0
            }  // up
            1 -> {// right
                go(1)
                direction = 1
            }
            2 -> {// down
                go(2)
                direction = 2
            }
            3 -> {// left
                go(3)
                direction = 3
            }
            4 -> rotationLeft()
            5 -> rotationRight()
        }
    }

    fun setPrincessImage(view: ImageView, win: TextView, princessContext: Context) {
        this.princessImg = view
        this.win = win
        this.princessContext = princessContext
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
        //princessImg.


        val animation =
            AnimationUtils.loadAnimation(princessContext, com.mashup.friendlycoding.R.anim.rotate)
        princessImg!!.startAnimation(animation)

//        princessImg!!.scaleType = ImageView.ScaleType.MATRIX   //required
//        matrix.postRotate(angle.toFloat(), 50F, 50F)
//        princessImg!!.imageMatrix = matrix
    }

    private fun rotationRight() {
        // TODO : 공주 사진 변경
    }

    private fun go(direction: Int) {
        val one = oneBlock
        when (direction) {
            //goint up
            0 -> {
                princessImg!!.y = (princessImg!!.y - one)

            }

            //going right
            1 -> {
                princessImg!!.x = (princessImg!!.x + one)
            }

            //going down
            2 -> {
                princessImg!!.y = (princessImg!!.y + one)
            }

            //going left
            3 -> {
                princessImg!!.x = (princessImg!!.x - one)

            }
        }
    }

    fun clear() {//위치 조정
        princessImg!!.x = oneBlock * 0 + oneBlock * 0.05f
        princessImg!!.y = oneBlock * 9// - oneBlock * 0.02f
    }
}