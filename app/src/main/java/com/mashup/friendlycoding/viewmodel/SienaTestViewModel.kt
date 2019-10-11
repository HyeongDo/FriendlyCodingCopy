package com.mashup.friendlycoding.viewmodel

import android.widget.ImageView
import androidx.lifecycle.ViewModel


class SienaTestViewModel : ViewModel() {

    var princessImg: ImageView? = null
    var xy = true
    var unit = 1

    fun setPrincessImage(view: ImageView) {
        this.princessImg = view
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
    fun changeX(){
        for (i in 0..50)
        princessImg!!.x = (princessImg!!.x + 4*unit).toFloat()
        Thread.sleep(100)

    }
    fun changeY(){
        for (i in 0..50)
            princessImg!!.y = (princessImg!!.y + 4*unit).toFloat()
        Thread.sleep(100)


    }
}