package com.mashup.friendlycoding.viewmodel

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel


class SienaTestViewModel : ViewModel() {

    var princessImg: ImageView? = null
    var xy = true
    var unit = 1
    var width = 0;
    var height = 0;

    fun setPrincessImage(view: ImageView) {
        this.princessImg = view
    }

    fun setViewSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }


    fun go(): Unit {
        if (princessImg != null)
            if (xy)
                changeX()
            else
                changeY()
    }

    fun rotation(): Unit {
        xy = !xy
        unit = if (!xy) -unit else unit
    }

    fun changeX() {
        princessImg!!.x = (princessImg!!.x + width/10 * unit)
        Thread.sleep(100)
        Log.e("123123", "" + princessImg!!.x)

    }

    fun changeY() {
        princessImg!!.y = (princessImg!!.y + height/10 * unit)
        Thread.sleep(100)
        Log.e("123123", "" + princessImg!!.y)


    }
}