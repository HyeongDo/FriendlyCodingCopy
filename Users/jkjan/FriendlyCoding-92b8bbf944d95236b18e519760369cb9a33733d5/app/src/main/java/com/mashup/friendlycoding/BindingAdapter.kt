package com.mashup.friendlycoding

import android.os.Handler
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import java.util.*

// 이미지 소스를 정할 때 앞으론
// android:src = "@drawable/~~" 보다
// ViewModel에 Int 형으로 drawable 을 저장한 후
// android:src = "@{~~VM.drawble}} 식으로 할 것
@BindingAdapter ("android:src")
fun ImageView.imgload (resId : Int) {
    this.setImageResource(resId)
}

// 위 아래로 움직이는 애니메이션
@BindingAdapter("android:animation")
fun ImageView.moveObjects(maxHeight : Int) {
    val timer = Timer()
    val handler = Handler()
    val originalX = this.y
    val originalY = this.y
    val view = this
    var direction = true
    timer.schedule(object : TimerTask() {
        override fun run() {
            handler.post(object : Runnable {
                override
                fun run() {
                    if (view.y < originalY - maxHeight || view.y >= originalY)
                        direction = !direction

                    if (direction) {
                        view.animate().translationY(100f).duration = 2000
                    }
                    else
                        view.animate().translationY(-100f).duration = 2000
                }
            })
        }
    }, 0, 500)
}