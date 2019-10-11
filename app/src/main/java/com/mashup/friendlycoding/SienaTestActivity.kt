package com.mashup.friendlycoding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.mashup.friendlycoding.databinding.ActivitySienaTestBinding
import com.mashup.friendlycoding.viewmodel.SienaTestViewModel

class SienaTestActivity : AppCompatActivity() {

    private var mSienaTestViewModel = SienaTestViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySienaTestBinding>(this, R.layout.activity_siena_test)
        binding.lifecycleOwner = this
        binding.testSiena = mSienaTestViewModel
        mSienaTestViewModel.setPrincessImage(binding.ivPrinsecess)


    }
}
