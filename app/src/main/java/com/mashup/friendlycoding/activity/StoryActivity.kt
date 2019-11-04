package com.mashup.friendlycoding.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.databinding.ActivityStoryBinding
import com.mashup.friendlycoding.viewmodel.StoryViewModel

class StoryActivity : BaseActivity() {
    private var mStoryViewModel = StoryViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityStoryBinding>(this,
            R.layout.activity_story
        )
        binding.lifecycleOwner = this
        binding.storyVM = mStoryViewModel
    }
}
