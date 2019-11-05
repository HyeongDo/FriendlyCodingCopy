package com.mashup.friendlycoding.activity

import android.content.Context
import android.os.Bundle
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.adapter.StageAdapter
import com.mashup.friendlycoding.databinding.ActivityMainBinding
import com.mashup.friendlycoding.model.StageItem
import com.mashup.friendlycoding.viewmodel.StageViewModel
import kotlinx.android.synthetic.main.activity_select_stage.*

class SelectStageActivity : BaseActivity() {

    var check = 0
    var key = "key"
    var up = -500F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_stage)

        val list = arrayListOf<StageItem>(
            StageItem("R.drawable.cave"),
            StageItem("R.drawable.cave"),
            StageItem("R.drawable.cave"),
            StageItem("R.drawable.cave"),
            StageItem("R.drawable.cave")
        )
        val adapter = StageAdapter(this, list)
        recyclerviewStage.adapter = adapter

        val lm = LinearLayoutManager(this)
        recyclerviewStage.layoutManager = lm
        recyclerviewStage.scrollToPosition(list.size - 1)

        if (check == 0) {
            recyclerviewStage.scrollToPosition(list.size - 1)
            recyclerviewStage.isLayoutFrozen = true
        } else if (check == 1) {
            recyclerviewStage.scrollToPosition(list.size - 2)
            up = -700F
            animateCloud()
            recyclerviewStage.isLayoutFrozen = true
        } else if (check == 2) {
            recyclerviewStage.scrollToPosition(list.size - 3)
            up = -1000F
            animateCloud()
            recyclerviewStage.isLayoutFrozen = true
        } else if (check == 3) {
            recyclerviewStage.scrollToPosition(list.size - 5)
            up = -700F
            animateCloud()
            //recyclerviewStage.isLayoutFrozen = true
        } else if (check == 4) {
            recyclerviewStage.scrollToPosition(list.size - 5)
            up = -900F
            animateCloud()
        }
    }

    override fun onPause() {
        super.onPause()
        saveStage(key, check)
    }

    override fun onResume() {
        super.onResume()
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        check = pref.getInt(key, 0)
    }

    fun saveStage(key: String, value: Int) {
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun animateCloud() {
        val cloud: ImageView = findViewById(R.id.cloud)
        val ani = TranslateAnimation(0F, 0F, 0F, up)
        ani.duration = 2000
        ani.fillAfter = true
        ani.startOffset = 2000
        cloud.startAnimation(ani)
    }
}
