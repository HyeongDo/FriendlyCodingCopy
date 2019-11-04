package com.mashup.friendlycoding.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.databinding.ActivitySelectStageBinding
import com.mashup.friendlycoding.viewmodel.StageViewModel

class SelectStageActivity : BaseActivity() {

    private var mStageViewModel = StageViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_stage)
        val binding = DataBindingUtil.setContentView<ActivitySelectStageBinding>(this,
            R.layout.activity_select_stage
        )
        binding.lifecycleOwner = this
        binding.stageVM = mStageViewModel

    }
}
