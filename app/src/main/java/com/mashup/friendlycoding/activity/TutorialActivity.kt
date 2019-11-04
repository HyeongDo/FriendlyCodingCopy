package com.mashup.friendlycoding.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.databinding.ActivityTutorialBinding
import com.mashup.friendlycoding.viewmodel.TutorialViewModel

class TutorialActivity : BaseActivity() {
    private var mTutorialViewModel = TutorialViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityTutorialBinding>(
                this,
                R.layout.activity_tutorial
            )
        binding.lifecycleOwner = this
        binding.tutorialVM = mTutorialViewModel
    }
}
