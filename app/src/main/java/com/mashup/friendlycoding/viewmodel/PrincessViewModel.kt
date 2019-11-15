package com.mashup.friendlycoding.viewmodel

import android.content.Context
import android.content.res.Resources
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mashup.friendlycoding.R

class PrincessViewModel : ViewModel() {
    var metBoss = MutableLiveData<Boolean>()

    private var princessImg: ImageView? = null
    private var win: TextView? = null
    private var oneBlock = 0f
    private val n = 10
    var width = 0

    private var direction = 0

    private var princessContext: Context? = null

    fun move(i: Int) {
        when (i) {
            -1 -> clear()
            0 -> {
                go(0)
                direction = 0
            }// up
            1 -> {
                go(1)
                direction = 1
            }  // right
            2 -> {
                go(2)
                direction = 2
            }  // down
            3 -> {
                go(3)
                direction = 3
            }  // left
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
        val animation =
            AnimationUtils.loadAnimation(princessContext, R.anim.rotate_left)
        princessImg!!.startAnimation(animation)
    }

    private fun rotationRight() {
        // TODO : 공주 사진 변경
        val animation =
            AnimationUtils.loadAnimation(princessContext, R.anim.rotate_right)
        princessImg!!.startAnimation(animation)


        //코드로 애니메이션 적용
//        var screenH=Resources.getSystem().displayMetrics.heightPixels
//        var screenW=Resources.getSystem().displayMetrics.widthPixels
//
//        var x = princessImg!!.pivotX/screenH
//
//        var y  = princessImg!!.pivotX/screenW
//
//        var rotateAnim = RotateAnimation(
//            0.0f, 90.0f, Animation.RELATIVE_TO_PARENT, x, Animation.RELATIVE_TO_PARENT, y
//        )
//
//        rotateAnim.duration = 300
//        rotateAnim.fillAfter = true
//
//        princessImg!!.startAnimation(rotateAnim)
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

    fun clear() {
        princessImg!!.x = oneBlock * 0 - oneBlock * 0.05f
        princessImg!!.y = oneBlock * 9 + oneBlock * 0.1f
    }
}